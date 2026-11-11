package codenomads.expensemanagement.service;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.UserExpenseSummary;
import codenomads.expensemanagement.gateway.TripGateway;
import codenomads.expensemanagement.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Step D of the test plan, Capability 3: getUserSummary.
 *
 * Independence pairs (all other inputs and fixture fields stay fixed):
 * A (tripId == null): D1/D2; B (userId == null): D1/D3;
 * C (fromInclusive == null): D1/D4; D (toExclusive == null): D1/D5.
 * E (fromInclusive.isAfter(toExclusive)): D1/D6.
 * P (payer matches): D1/D7; Rs (share responsibility): D1/D8;
 * Rc (category responsibility): D1/D8; K (source matches HOTEL): D1/D9.
 *
 * Java skips null-guard operands after the first true operand. Each target
 * operand is reached in its pair. Rs and Rc are separate decision sites;
 * Rc and K are sequential filters, and K is skipped when Rc is false.
 * Every split here is 100.00 / 2, so rounding is outside this suite's scope.
 */
@ExtendWith(MockitoExtension.class)
class GetUserSummaryMCDCTest {

    private static final Long TRIP_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final LocalDate FROM = LocalDate.of(2025, 1, 1);
    private static final LocalDate TO = LocalDate.of(2025, 2, 1);

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense;

    @BeforeEach
    void setUp() {
        expense = new Expense();
        expense.setId(1L);
        expense.setTripId(TRIP_ID);
        expense.setAmount(new BigDecimal("100.00"));
        expense.setDescription("MC/DC reference expense");
        expense.setCreatedByUserId(USER_ID);
        expense.setResponsibleUserIds(Set.of(USER_ID, 2L));
        expense.setSource(Expense.ExpenseSource.HOTEL);
        expense.setOccurredOn(LocalDate.of(2025, 1, 15));
    }

    @Test
    @DisplayName("D1: all parameters present and all reference predicates true")
    void d1ReferenceSummary() {
        UserExpenseSummary result = getSummaryForFixture();

        assertSummary(result, "100.00", "50.00", "50.00", "50.00", "0.00");
    }

    @Test
    @DisplayName("D2: only tripId is null (A independence pair D1/D2)")
    void d2OnlyTripIdNull() {
        assertRejected(null, USER_ID, FROM, TO, "All parameters must be non-null");
    }

    @Test
    @DisplayName("D3: only userId is null (B independence pair D1/D3)")
    void d3OnlyUserIdNull() {
        assertRejected(TRIP_ID, null, FROM, TO, "All parameters must be non-null");
    }

    @Test
    @DisplayName("D4: only fromInclusive is null (C independence pair D1/D4)")
    void d4OnlyStartDateNull() {
        assertRejected(TRIP_ID, USER_ID, null, TO, "All parameters must be non-null");
    }

    @Test
    @DisplayName("D5: only toExclusive is null (D independence pair D1/D5)")
    void d5OnlyEndDateNull() {
        assertRejected(TRIP_ID, USER_ID, FROM, null, "All parameters must be non-null");
    }

    @Test
    @DisplayName("D6: only end date moves before start (E independence pair D1/D6)")
    void d6EndDateBeforeStart() {
        assertRejected(TRIP_ID, USER_ID, FROM, FROM.minusDays(1),
            "fromInclusive must be before or equal to toExclusive");
    }

    @Test
    @DisplayName("D7: only payer changes (P independence pair D1/D7)")
    void d7OtherUserPays() {
        expense.setCreatedByUserId(2L);

        UserExpenseSummary result = getSummaryForFixture();

        assertSummary(result, "0.00", "50.00", "-50.00", "50.00", "0.00");
    }

    @Test
    @DisplayName("D8: only responsible users change (Rs and Rc pairs D1/D8)")
    void d8UserPaysButDoesNotShareCost() {
        expense.setResponsibleUserIds(Set.of(2L, 3L));

        UserExpenseSummary result = getSummaryForFixture();

        assertSummary(result, "100.00", "0.00", "100.00", "0.00", "0.00");
    }

    @Test
    @DisplayName("D9: only source changes to FLIGHT (K at HOTEL pair D1/D9)")
    void d9FlightInsteadOfHotel() {
        expense.setSource(Expense.ExpenseSource.FLIGHT);

        UserExpenseSummary result = getSummaryForFixture();

        assertSummary(result, "100.00", "50.00", "50.00", "0.00", "50.00");
    }

    private UserExpenseSummary getSummaryForFixture() {
        when(expenseRepository.findByTripIdAndOccurredOnBetween(TRIP_ID, FROM, TO))
            .thenReturn(List.of(expense));

        UserExpenseSummary result = expenseService.getUserSummary(TRIP_ID, USER_ID, FROM, TO);

        verify(expenseRepository).findByTripIdAndOccurredOnBetween(TRIP_ID, FROM, TO);
        verifyNoMoreInteractions(expenseRepository);
        verifyNoInteractions(tripGateway);
        return result;
    }

    private void assertRejected(Long tripId, Long userId, LocalDate from, LocalDate to,
                                String expectedMessage) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(tripId, userId, from, to));

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(expenseRepository, tripGateway);
    }

    private void assertSummary(UserExpenseSummary result, String paid, String share, String net,
                               String hotelShare, String flightShare) {
        assertAll(
            () -> assertEquals(new BigDecimal(paid), result.paidByUser(), "paid total"),
            () -> assertEquals(new BigDecimal(share), result.shareOfUser(), "user share"),
            () -> assertEquals(new BigDecimal(net), result.net(), "net balance"),
            () -> assertEquals(Map.of(
                Expense.ExpenseSource.HOTEL, new BigDecimal(hotelShare),
                Expense.ExpenseSource.FLIGHT, new BigDecimal(flightShare),
                Expense.ExpenseSource.CARHIRE, new BigDecimal("0.00"),
                Expense.ExpenseSource.EXPERIENCE, new BigDecimal("0.00")
            ), result.byCategory(), "shares for every category")
        );
    }
}

package codenomads.expensemanagement.service;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.domain.Expense.ExpenseSource;
import codenomads.expensemanagement.dto.UserExpenseSummary;
import codenomads.expensemanagement.gateway.TripGateway;
import codenomads.expensemanagement.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static codenomads.expensemanagement.domain.Expense.ExpenseSource.CARHIRE;
import static codenomads.expensemanagement.domain.Expense.ExpenseSource.EXPERIENCE;
import static codenomads.expensemanagement.domain.Expense.ExpenseSource.FLIGHT;
import static codenomads.expensemanagement.domain.Expense.ExpenseSource.HOTEL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripExpenseSummaryPairwiseTest {

    private static final long TRIP_ID = 71L;
    private static final long SUMMARY_USER_ID = 101L;
    private static final long OTHER_MEMBER_ID = 102L;
    private static final Set<Long> SUMMARY_USER_ONLY = Set.of(SUMMARY_USER_ID);
    private static final Set<Long> SUMMARY_USER_AND_OTHER_MEMBER = Set.of(SUMMARY_USER_ID, OTHER_MEMBER_ID);
    private static final Set<Long> OTHER_MEMBERS_ONLY = Set.of(OTHER_MEMBER_ID, 103L);

    private static final LocalDate FROM_INCLUSIVE = LocalDate.of(2026, 10, 1);
    private static final LocalDate DATE_INSIDE_RANGE = LocalDate.of(2026, 10, 8);
    private static final LocalDate TO_EXCLUSIVE = LocalDate.of(2026, 10, 15);

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final Map<ExpenseSource, BigDecimal> ZERO_SHARES =
        Map.of(HOTEL, ZERO, FLIGHT, ZERO, CARHIRE, ZERO, EXPERIENCE, ZERO);
    private static final UserExpenseSummary EMPTY_SUMMARY =
        new UserExpenseSummary(ZERO, ZERO, ZERO, ZERO_SHARES);

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void combinesPaymentsAndSharesWithinAndAcrossCategories() {
        when(expenseRepository.findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE))
            .thenReturn(List.of(
                expense("120.00", OTHER_MEMBER_ID, SUMMARY_USER_ONLY, HOTEL, FROM_INCLUSIVE),
                expense("30.00", SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, HOTEL, DATE_INSIDE_RANGE),
                expense("50.00", SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, FLIGHT, DATE_INSIDE_RANGE)
            ));

        UserExpenseSummary summary = expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);

        assertAll(
            () -> assertEquals(new BigDecimal("80.00"), summary.paidByUser(), "paid"),
            () -> assertEquals(new BigDecimal("160.00"), summary.shareOfUser(), "owed"),
            () -> assertEquals(new BigDecimal("-80.00"), summary.net(), "net"),
            () -> assertEquals(Map.of(HOTEL, new BigDecimal("135.00"), FLIGHT, new BigDecimal("25.00"),
                CARHIRE, ZERO, EXPERIENCE, ZERO), summary.byCategory(), "category shares"),
            () -> verify(expenseRepository).findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("singleExpenseScenarios")
    void summarizesOneExpense(
        String scenario, long payerUserId, Set<Long> responsibleUserIds,
        ExpenseSource category, LocalDate expenseDate, String expectedPaid,
        String expectedOwed, String expectedNet
    ) {
        when(expenseRepository.findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE))
            .thenReturn(List.of(expense("120.00", payerUserId, responsibleUserIds, category, expenseDate)));

        UserExpenseSummary summary = expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);

        Map<ExpenseSource, BigDecimal> categoryShares = new EnumMap<>(ZERO_SHARES);
        categoryShares.put(category, new BigDecimal(expectedOwed));
        assertAll(scenario,
            () -> assertEquals(new BigDecimal(expectedPaid), summary.paidByUser(), "paid"),
            () -> assertEquals(new BigDecimal(expectedOwed), summary.shareOfUser(), "owed"),
            () -> assertEquals(new BigDecimal(expectedNet), summary.net(), "net"),
            () -> assertEquals(categoryShares, summary.byCategory(), "category shares"),
            () -> verify(expenseRepository).findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE)
        );
    }

    private static Stream<Arguments> singleExpenseScenarios() {
        // Scenario, payer ID, responsible IDs, category, date, expected paid, owed, net.
        return Stream.of(
            arguments("User pays and shares the hotel expense",
                SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, HOTEL, DATE_INSIDE_RANGE, "120.00", "60.00", "60.00"),
            arguments("User pays a hotel expense owed by others",
                SUMMARY_USER_ID, OTHER_MEMBERS_ONLY, HOTEL, DATE_INSIDE_RANGE, "120.00", "0.00", "120.00"),
            arguments("User owes the full flight expense paid by another member",
                OTHER_MEMBER_ID, SUMMARY_USER_ONLY, FLIGHT, FROM_INCLUSIVE, "0.00", "120.00", "-120.00"),
            arguments("User pays and shares the flight expense",
                SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, FLIGHT, DATE_INSIDE_RANGE, "120.00", "60.00", "60.00"),
            arguments("Other members pay and owe the flight expense",
                OTHER_MEMBER_ID, OTHER_MEMBERS_ONLY, FLIGHT, FROM_INCLUSIVE, "0.00", "0.00", "0.00"),
            arguments("User pays and owes the full car hire expense",
                SUMMARY_USER_ID, SUMMARY_USER_ONLY, CARHIRE, DATE_INSIDE_RANGE, "120.00", "120.00", "0.00"),
            arguments("User shares a car hire expense paid by another member",
                OTHER_MEMBER_ID, SUMMARY_USER_AND_OTHER_MEMBER, CARHIRE, FROM_INCLUSIVE, "0.00", "60.00", "-60.00"),
            arguments("User pays a car hire expense owed by others",
                SUMMARY_USER_ID, OTHER_MEMBERS_ONLY, CARHIRE, DATE_INSIDE_RANGE, "120.00", "0.00", "120.00"),
            arguments("User pays and owes the full experience expense",
                SUMMARY_USER_ID, SUMMARY_USER_ONLY, EXPERIENCE, FROM_INCLUSIVE, "120.00", "120.00", "0.00"),
            arguments("User shares an experience expense paid by another member",
                OTHER_MEMBER_ID, SUMMARY_USER_AND_OTHER_MEMBER, EXPERIENCE, DATE_INSIDE_RANGE, "0.00", "60.00", "-60.00"),
            arguments("Other members pay and owe the experience expense",
                OTHER_MEMBER_ID, OTHER_MEMBERS_ONLY, EXPERIENCE, FROM_INCLUSIVE, "0.00", "0.00", "0.00")
        );
    }

    @Test
    void returnsZeroWhenNoExpensesMatchTheQuery() {
        when(expenseRepository.findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE))
            .thenReturn(List.of());

        UserExpenseSummary summary = expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);

        assertAll(
            () -> assertEquals(EMPTY_SUMMARY, summary),
            () -> verify(expenseRepository).findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE)
        );
    }

    @Test
    void returnsZeroWhenStartAndEndAreEqual() {
        assertEquals(EMPTY_SUMMARY, expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE));
    }

    @Test
    void rejectsAnEndDateBeforeTheStartDate() {
        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE.minusDays(1)));
        verifyNoInteractions(expenseRepository, tripGateway);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("missingQueryArguments")
    void rejectsMissingQueryArguments(String scenario, Long tripId, Long summaryUserId,
        LocalDate fromInclusive, LocalDate toExclusive) {
        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(tripId, summaryUserId, fromInclusive, toExclusive), scenario);
        verifyNoInteractions(expenseRepository, tripGateway);
    }

    private static Stream<Arguments> missingQueryArguments() {
        return Stream.of(
            arguments("Trip ID is missing", null, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE),
            arguments("Summary user ID is missing", TRIP_ID, null, FROM_INCLUSIVE, TO_EXCLUSIVE),
            arguments("Inclusive start is missing", TRIP_ID, SUMMARY_USER_ID, null, TO_EXCLUSIVE),
            arguments("Exclusive end is missing", TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, null),
            arguments("All query arguments are missing", null, null, null, null)
        );
    }

    private static Expense expense(String amount, long payerUserId, Set<Long> responsibleUserIds,
        ExpenseSource category, LocalDate expenseDate) {
        Expense expense = new Expense();
        expense.setTripId(TRIP_ID);
        expense.setAmount(new BigDecimal(amount));
        expense.setCreatedByUserId(payerUserId);
        expense.setResponsibleUserIds(responsibleUserIds);
        expense.setSource(category);
        expense.setOccurredOn(expenseDate);
        return expense;
    }
}

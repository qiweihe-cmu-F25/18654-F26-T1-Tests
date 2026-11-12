package codenomads.expensemanagement.service;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripExpenseSummaryPairwiseTest {

    private static final long TRIP_ID = 71L;
    private static final long SUMMARY_USER_ID = 101L;
    private static final long OTHER_TRIP_MEMBER_ID = 102L;
    private static final long UNKNOWN_TRIP_ID = 999L;
    private static final long UNKNOWN_USER_ID = 999L;

    private static final LocalDate FROM_INCLUSIVE = LocalDate.of(2026, 10, 1);
    private static final LocalDate DATE_INSIDE_RANGE = LocalDate.of(2026, 10, 8);
    private static final LocalDate TO_EXCLUSIVE = LocalDate.of(2026, 10, 15);
    private static final LocalDate DAY_BEFORE_START = FROM_INCLUSIVE.minusDays(1);

    private static final String MISSING_ARGUMENT_MESSAGE = "All parameters must be non-null";
    private static final String REVERSED_RANGE_MESSAGE =
        "fromInclusive must be before or equal to toExclusive";

    private static final Set<Long> HOTEL_RESPONSIBLE_USER_IDS =
        Set.of(SUMMARY_USER_ID, OTHER_TRIP_MEMBER_ID);
    private static final BigDecimal HOTEL_AMOUNT = new BigDecimal("120.00");
    private static final BigDecimal HOTEL_SHARE_PER_USER = new BigDecimal("60.00");
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    private static final UserExpenseSummary HOTEL_SUMMARY = new UserExpenseSummary(
        HOTEL_AMOUNT, HOTEL_SHARE_PER_USER, HOTEL_SHARE_PER_USER,
        Map.of(HOTEL, HOTEL_SHARE_PER_USER, FLIGHT, ZERO, CARHIRE, ZERO, EXPERIENCE, ZERO)
    );

    private static final UserExpenseSummary EMPTY_SUMMARY = new UserExpenseSummary(
        ZERO, ZERO, ZERO, Map.of(HOTEL, ZERO, FLIGHT, ZERO, CARHIRE, ZERO, EXPERIENCE, ZERO)
    );

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExpenseService expenseService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidSummaryQueries")
    void rejectsInvalidSummaryQueriesBeforeAccessingDependencies(
        String scenario, Long tripId, Long summaryUserId,
        LocalDate fromInclusive, LocalDate toExclusive, String expectedMessage
    ) {
        IllegalArgumentException rejection = assertThrows(
            IllegalArgumentException.class,
            () -> expenseService.getUserSummary(tripId, summaryUserId, fromInclusive, toExclusive),
            scenario
        );

        assertEquals(expectedMessage, rejection.getMessage());
        verifyNoInteractions(expenseRepository, tripGateway);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("dateRangeScenarios")
    void summarizesExpensesReturnedForTheRequestedRange(
        String scenario, List<Expense> matchingExpenses, UserExpenseSummary expectedSummary
    ) {
        assertSummary(scenario, expectedSummary, querySummaryWithMatches(matchingExpenses));
    }

    @Test
    void summarizesAnExpenseSavedWithTheDefaultDate() {
        when(tripGateway.isUserInTrip(TRIP_ID, SUMMARY_USER_ID)).thenReturn(true);
        when(tripGateway.isUserInTrip(TRIP_ID, OTHER_TRIP_MEMBER_ID)).thenReturn(true);
        when(expenseRepository.save(any(Expense.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        AddExpenseRequest request = new AddExpenseRequest();
        request.setTripId(TRIP_ID);
        request.setCreatedByUserId(SUMMARY_USER_ID);
        request.setResponsibleUserIds(HOTEL_RESPONSIBLE_USER_IDS);
        request.setAmount(HOTEL_AMOUNT);
        request.setSource(HOTEL);

        Expense savedExpense;
        try (MockedStatic<LocalDate> clock = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            clock.when(LocalDate::now).thenReturn(DATE_INSIDE_RANGE);
            savedExpense = expenseService.addExpense(request);
        }

        assertEquals(DATE_INSIDE_RANGE, savedExpense.getOccurredOn(), "saved occurrence date");
        verify(expenseRepository).save(savedExpense);

        UserExpenseSummary actualSummary = querySummaryWithMatches(List.of(savedExpense));
        assertSummary("expense saved with the default date", HOTEL_SUMMARY, actualSummary);
    }

    @Test
    void returnsAnEmptySummaryForAnUnknownUserWhenBoundsAreEqual() {
        // The repository mock returns an empty list if the service queries the empty window.
        UserExpenseSummary actualSummary = expenseService.getUserSummary(
            TRIP_ID, UNKNOWN_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE);

        assertSummary("empty date range", EMPTY_SUMMARY, actualSummary);
        verifyNoInteractions(tripGateway);
    }

    private static Stream<Arguments> invalidSummaryQueries() {
        // Expense dates distinguish some plan rows with identical request arguments.
        // Rejected requests stop before any expense is loaded.
        return Stream.of(
            arguments("All required arguments are missing",
                null, null, null, null, MISSING_ARGUMENT_MESSAGE),
            arguments("Start date is missing for an unknown trip and user",
                UNKNOWN_TRIP_ID, UNKNOWN_USER_ID, null, TO_EXCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("User is missing with reversed dates and a planned expense at the end",
                UNKNOWN_TRIP_ID, null, FROM_INCLUSIVE, DAY_BEFORE_START, MISSING_ARGUMENT_MESSAGE),
            arguments("Trip is missing with equal bounds and a planned expense at the start",
                null, UNKNOWN_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("End date is missing for an unknown user",
                TRIP_ID, UNKNOWN_USER_ID, FROM_INCLUSIVE, null, MISSING_ARGUMENT_MESSAGE),
            arguments("Both bounds are missing for an unknown trip",
                UNKNOWN_TRIP_ID, SUMMARY_USER_ID, null, null, MISSING_ARGUMENT_MESSAGE),
            arguments("User is missing with equal bounds",
                TRIP_ID, null, FROM_INCLUSIVE, FROM_INCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("User is missing for an unknown trip with equal bounds",
                UNKNOWN_TRIP_ID, null, FROM_INCLUSIVE, FROM_INCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("End precedes start for an unknown user",
                TRIP_ID, UNKNOWN_USER_ID, FROM_INCLUSIVE, DAY_BEFORE_START, REVERSED_RANGE_MESSAGE),
            arguments("Trip is missing for an unknown user with a forward date range",
                null, UNKNOWN_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("Trip is missing with reversed dates",
                null, SUMMARY_USER_ID, FROM_INCLUSIVE, DAY_BEFORE_START, MISSING_ARGUMENT_MESSAGE),
            arguments("User is missing for an unknown trip with a forward date range",
                UNKNOWN_TRIP_ID, null, FROM_INCLUSIVE, TO_EXCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("Trip is missing with equal bounds for the summary user",
                null, SUMMARY_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("User is missing with reversed dates and a planned expense at the start",
                UNKNOWN_TRIP_ID, null, FROM_INCLUSIVE, DAY_BEFORE_START, MISSING_ARGUMENT_MESSAGE),
            arguments("Trip is missing with equal bounds and a planned expense at the end",
                null, UNKNOWN_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE, MISSING_ARGUMENT_MESSAGE),
            arguments("Both bounds are missing for an unknown user",
                TRIP_ID, UNKNOWN_USER_ID, null, null, MISSING_ARGUMENT_MESSAGE),
            arguments("End precedes start for the summary user",
                TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, DAY_BEFORE_START, REVERSED_RANGE_MESSAGE),
            arguments("End precedes start for an unknown trip",
                UNKNOWN_TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, DAY_BEFORE_START,
                REVERSED_RANGE_MESSAGE)
        );
    }

    private static Stream<Arguments> dateRangeScenarios() {
        return Stream.of(
            arguments("Repository returns a hotel expense inside the range",
                List.of(hotelExpenseOn(DATE_INSIDE_RANGE)), HOTEL_SUMMARY),
            arguments("No match for an expense before the start on " + DAY_BEFORE_START,
                List.of(), EMPTY_SUMMARY),
            arguments("Repository returns a hotel expense on the inclusive start",
                List.of(hotelExpenseOn(FROM_INCLUSIVE)), HOTEL_SUMMARY),
            arguments("No match for an expense on the exclusive end on " + TO_EXCLUSIVE,
                List.of(), EMPTY_SUMMARY),
            arguments("No match for an expense after the end on " + TO_EXCLUSIVE.plusDays(1),
                List.of(), EMPTY_SUMMARY)
        );
    }

    private UserExpenseSummary querySummaryWithMatches(List<Expense> matchingExpenses) {
        when(expenseRepository.findByTripIdAndOccurredOnBetween(
            TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE)).thenReturn(matchingExpenses);

        UserExpenseSummary summary = expenseService.getUserSummary(
            TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);

        verify(expenseRepository)
            .findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);
        return summary;
    }

    private static Expense hotelExpenseOn(LocalDate expenseDate) {
        Expense expense = new Expense();
        expense.setTripId(TRIP_ID);
        expense.setCreatedByUserId(SUMMARY_USER_ID);
        expense.setResponsibleUserIds(HOTEL_RESPONSIBLE_USER_IDS);
        expense.setAmount(HOTEL_AMOUNT);
        expense.setSource(HOTEL);
        expense.setOccurredOn(expenseDate);
        return expense;
    }

    private static void assertSummary(
        String scenario, UserExpenseSummary expected, UserExpenseSummary actual
    ) {
        assertAll(scenario,
            () -> assertEquals(expected.paidByUser(), actual.paidByUser(), "paid by user"),
            () -> assertEquals(expected.shareOfUser(), actual.shareOfUser(), "owed by user"),
            () -> assertEquals(expected.net(), actual.net(), "net balance"),
            () -> assertEquals(expected.byCategory(), actual.byCategory(), "shares by category")
        );
    }
}

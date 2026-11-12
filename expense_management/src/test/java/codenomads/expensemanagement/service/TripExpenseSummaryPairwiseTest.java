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
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripExpenseSummaryPairwiseTest {

    private static final long TRIP_ID = 71L;
    private static final long SUMMARY_USER_ID = 101L;
    private static final long OTHER_MEMBER_ID = 102L;
    private static final long THIRD_MEMBER_ID = 103L;

    private static final LocalDate FROM_INCLUSIVE = LocalDate.of(2026, 10, 1);
    private static final LocalDate DATE_INSIDE_WINDOW = LocalDate.of(2026, 10, 8);
    private static final LocalDate TO_EXCLUSIVE = LocalDate.of(2026, 10, 15);

    private static final Set<Long> SUMMARY_USER_ONLY = Set.of(SUMMARY_USER_ID);
    private static final Set<Long> SUMMARY_USER_AND_OTHER_MEMBER =
        Set.of(SUMMARY_USER_ID, OTHER_MEMBER_ID);
    private static final Set<Long> OTHER_MEMBERS_ONLY =
        Set.of(OTHER_MEMBER_ID, THIRD_MEMBER_ID);

    private static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final Map<ExpenseSource, BigDecimal> ZERO_SHARES_BY_CATEGORY =
        Map.of(HOTEL, ZERO, FLIGHT, ZERO, CARHIRE, ZERO, EXPERIENCE, ZERO);
    private static final UserExpenseSummary EMPTY_SUMMARY =
        new UserExpenseSummary(ZERO, ZERO, ZERO, ZERO_SHARES_BY_CATEGORY);

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExpenseService expenseService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("expenseSummaryScenarios")
    void summarizesMatchingTripExpenses(
        List<Expense> matchingExpenses, UserExpenseSummary expectedSummary
    ) {
        when(expenseRepository.findByTripIdAndOccurredOnBetween(
            TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE
        )).thenReturn(matchingExpenses);

        UserExpenseSummary actualSummary =
            expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);

        assertExpenseSummary(expectedSummary, actualSummary);
        verify(expenseRepository)
            .findByTripIdAndOccurredOnBetween(TRIP_ID, FROM_INCLUSIVE, TO_EXCLUSIVE);
    }

    @Test
    void returnsEmptySummaryWhenStartAndEndDatesAreEqual() {
        // If the service queries this empty window, the repository mock returns an empty list.
        UserExpenseSummary actualSummary =
            expenseService.getUserSummary(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, FROM_INCLUSIVE);

        assertExpenseSummary(EMPTY_SUMMARY, actualSummary);
    }

    @Test
    void rejectsAnEndDateBeforeTheStartDate() {
        LocalDate endBeforeStart = FROM_INCLUSIVE.minusDays(1);

        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(
                TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, endBeforeStart));

        verifyNoInteractions(expenseRepository, tripGateway);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("incompleteSummaryQueries")
    void rejectsMissingQueryArgumentsBeforeAccessingDependencies(SummaryQuery query) {
        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(
                query.tripId(), query.userId(), query.fromInclusive(), query.toExclusive()));

        verifyNoInteractions(expenseRepository, tripGateway);
    }

    private static Stream<Arguments> expenseSummaryScenarios() {
        return Stream.of(
            arguments(named("Combines multiple expenses within and across categories", List.of(
                    expense("120.00", OTHER_MEMBER_ID, SUMMARY_USER_ONLY, HOTEL, FROM_INCLUSIVE),
                    expense("30.00", SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, HOTEL, DATE_INSIDE_WINDOW),
                    expense("50.00", SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, FLIGHT, DATE_INSIDE_WINDOW)
                )),
                summary("80.00", "160.00", "-80.00", Map.of(HOTEL, "135.00", FLIGHT, "25.00"))),

            singleExpense("User pays and shares a hotel expense",
                expense("120.00", SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, HOTEL, DATE_INSIDE_WINDOW),
                summary("120.00", "60.00", "60.00", Map.of(HOTEL, "60.00"))),

            singleExpense("User pays a hotel expense owed by other members",
                expense("120.00", SUMMARY_USER_ID, OTHER_MEMBERS_ONLY, HOTEL, DATE_INSIDE_WINDOW),
                summary("120.00", "0.00", "120.00", Map.of())),

            singleExpense("User owes a flight paid by another member on the start date",
                expense("120.00", OTHER_MEMBER_ID, SUMMARY_USER_ONLY, FLIGHT, FROM_INCLUSIVE),
                summary("0.00", "120.00", "-120.00", Map.of(FLIGHT, "120.00"))),

            singleExpense("User pays and shares a flight expense",
                expense("120.00", SUMMARY_USER_ID, SUMMARY_USER_AND_OTHER_MEMBER, FLIGHT, DATE_INSIDE_WINDOW),
                summary("120.00", "60.00", "60.00", Map.of(FLIGHT, "60.00"))),

            singleExpense("Other members pay and owe a flight on the start date",
                expense("120.00", OTHER_MEMBER_ID, OTHER_MEMBERS_ONLY, FLIGHT, FROM_INCLUSIVE),
                EMPTY_SUMMARY),

            singleExpense("User pays and owes the full car hire expense",
                expense("120.00", SUMMARY_USER_ID, SUMMARY_USER_ONLY, CARHIRE, DATE_INSIDE_WINDOW),
                summary("120.00", "120.00", "0.00", Map.of(CARHIRE, "120.00"))),

            singleExpense("User shares a car hire expense paid by another member on the start date",
                expense("120.00", OTHER_MEMBER_ID, SUMMARY_USER_AND_OTHER_MEMBER, CARHIRE, FROM_INCLUSIVE),
                summary("0.00", "60.00", "-60.00", Map.of(CARHIRE, "60.00"))),

            singleExpense("User pays a car hire expense owed by other members",
                expense("120.00", SUMMARY_USER_ID, OTHER_MEMBERS_ONLY, CARHIRE, DATE_INSIDE_WINDOW),
                summary("120.00", "0.00", "120.00", Map.of())),

            singleExpense("User pays and owes the full experience expense on the start date",
                expense("120.00", SUMMARY_USER_ID, SUMMARY_USER_ONLY, EXPERIENCE, FROM_INCLUSIVE),
                summary("120.00", "120.00", "0.00", Map.of(EXPERIENCE, "120.00"))),

            singleExpense("User shares an experience expense paid by another member",
                expense("120.00", OTHER_MEMBER_ID, SUMMARY_USER_AND_OTHER_MEMBER, EXPERIENCE, DATE_INSIDE_WINDOW),
                summary("0.00", "60.00", "-60.00", Map.of(EXPERIENCE, "60.00"))),

            singleExpense("Other members pay and owe an experience on the start date",
                expense("120.00", OTHER_MEMBER_ID, OTHER_MEMBERS_ONLY, EXPERIENCE, FROM_INCLUSIVE),
                EMPTY_SUMMARY),

            arguments(named("No expenses match the requested window", List.<Expense>of()), EMPTY_SUMMARY)
        );
    }

    private static Stream<Arguments> incompleteSummaryQueries() {
        return Stream.of(
            arguments(named("Missing trip ID",
                new SummaryQuery(null, SUMMARY_USER_ID, FROM_INCLUSIVE, TO_EXCLUSIVE))),
            arguments(named("Missing summary user ID",
                new SummaryQuery(TRIP_ID, null, FROM_INCLUSIVE, TO_EXCLUSIVE))),
            arguments(named("Missing inclusive start date",
                new SummaryQuery(TRIP_ID, SUMMARY_USER_ID, null, TO_EXCLUSIVE))),
            arguments(named("Missing exclusive end date",
                new SummaryQuery(TRIP_ID, SUMMARY_USER_ID, FROM_INCLUSIVE, null))),
            arguments(named("Missing all query arguments",
                new SummaryQuery(null, null, null, null)))
        );
    }

    private static Arguments singleExpense(
        String scenario, Expense matchingExpense, UserExpenseSummary expectedSummary
    ) {
        return arguments(named(scenario, List.of(matchingExpense)), expectedSummary);
    }

    private static Expense expense(
        String amount, long payerUserId, Set<Long> responsibleUserIds,
        ExpenseSource category, LocalDate occurredOn
    ) {
        Expense expense = new Expense();
        expense.setTripId(TRIP_ID);
        expense.setAmount(new BigDecimal(amount));
        expense.setCreatedByUserId(payerUserId);
        expense.setResponsibleUserIds(responsibleUserIds);
        expense.setSource(category);
        expense.setOccurredOn(occurredOn);
        return expense;
    }

    private static UserExpenseSummary summary(
        String paid, String owed, String net, Map<ExpenseSource, String> sharesByCategory
    ) {
        Map<ExpenseSource, BigDecimal> categoryShares = new EnumMap<>(ZERO_SHARES_BY_CATEGORY);
        sharesByCategory.forEach((category, amount) ->
            categoryShares.put(category, new BigDecimal(amount)));
        return new UserExpenseSummary(
            new BigDecimal(paid), new BigDecimal(owed), new BigDecimal(net), categoryShares);
    }

    private static void assertExpenseSummary(
        UserExpenseSummary expected, UserExpenseSummary actual
    ) {
        assertAll("trip expense summary",
            () -> assertEquals(expected.paidByUser(), actual.paidByUser(), "paid by user"),
            () -> assertEquals(expected.shareOfUser(), actual.shareOfUser(), "owed by user"),
            () -> assertEquals(expected.net(), actual.net(), "net balance"),
            () -> assertEquals(expected.byCategory(), actual.byCategory(), "shares by category")
        );
    }

    private record SummaryQuery(
        Long tripId, Long userId, LocalDate fromInclusive, LocalDate toExclusive
    ) {}
}

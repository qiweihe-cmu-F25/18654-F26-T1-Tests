package codenomads.expensemanagement.dto;

import codenomads.expensemanagement.domain.Expense;

import java.math.BigDecimal;
import java.util.Map;

public record UserExpenseSummary(
    BigDecimal paidByUser,
    BigDecimal shareOfUser,
    BigDecimal net,
    Map<Expense.ExpenseSource, BigDecimal> byCategory
) {}
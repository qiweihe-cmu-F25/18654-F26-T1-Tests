package codenomads.expensemanagement.dto;

import java.math.BigDecimal;
import java.util.Map;

public record BalanceSheet(
    Map<Long, BigDecimal> netByUser
) {}
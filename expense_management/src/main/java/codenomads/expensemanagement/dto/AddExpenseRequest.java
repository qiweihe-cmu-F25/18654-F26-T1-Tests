package codenomads.expensemanagement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import codenomads.expensemanagement.domain.Expense;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddExpenseRequest {
    @NotNull(message = "Trip ID is required")
    private Long tripId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
    
    private String description;
    
    @NotEmpty(message = "At least one responsible user is required")
    private Set<Long> responsibleUserIds;
    
    @NotNull(message = "Expense source is required")
    private Expense.ExpenseSource source;
    
    @NotNull(message = "Created by user ID is required")
    private Long createdByUserId;
    
    private LocalDate occurredOn;
}

package codenomads.expensemanagement.controller;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
import codenomads.expensemanagement.dto.UserExpenseSummary;
import codenomads.expensemanagement.dto.BalanceSheet;
import codenomads.expensemanagement.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@Valid @RequestBody AddExpenseRequest addExpenseRequest) {
        Expense expense = expenseService.addExpense(addExpenseRequest);
        return ResponseEntity.ok(new ExpenseResponse(expense.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id, @Valid @RequestBody ExpenseRequest request) {
        Expense updatedExpense = expenseService.updateExpense(id, request.getAmount());
        return ResponseEntity.ok(new ExpenseResponse(updatedExpense.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeExpense(@PathVariable Long id) {
        expenseService.removeExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<BigDecimal> queryExpensesForUserInTrip(@RequestParam Long tripId, @RequestParam Long userId) {
        BigDecimal expenses = expenseService.queryExpensesForUserInTrip(tripId, userId);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/summary")
    public ResponseEntity<UserExpenseSummary> getUserSummary(
            @RequestParam Long tripId,
            @RequestParam Long userId,
            @RequestParam LocalDate fromInclusive,
            @RequestParam LocalDate toExclusive) {
        UserExpenseSummary summary = expenseService.getUserSummary(tripId, userId, fromInclusive, toExclusive);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/balance-sheet")
    public ResponseEntity<BalanceSheet> getBalanceSheet(@RequestParam Long tripId) {
        BalanceSheet balanceSheet = expenseService.getBalanceSheet(tripId);
        return ResponseEntity.ok(balanceSheet);
    }

    // Inner class to represent request payload
    @Data
    public static class ExpenseRequest {
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        private BigDecimal amount;
    }

    // Inner class to represent response payload
    @Data
    public static class ExpenseResponse {
        private Long expenseId;

        public ExpenseResponse(Long expenseId) {
            this.expenseId = expenseId;
        }
    }
}
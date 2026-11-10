package codenomads.expensemanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
import codenomads.expensemanagement.dto.UserExpenseSummary;
import codenomads.expensemanagement.dto.BalanceSheet;
import codenomads.expensemanagement.service.ExpenseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    private AddExpenseRequest testRequest;
    private Expense testExpense;

    @BeforeEach
    void setUp() {
        testRequest = createTestRequest();
        testExpense = createTestExpense();
    }

    @Test
    void addExpenseValidRequestShouldReturnOkWithExpenseId() {
        when(expenseService.addExpense(any(AddExpenseRequest.class))).thenReturn(testExpense);

        ResponseEntity<ExpenseController.ExpenseResponse> response = expenseController.addExpense(testRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testExpense.getId(), response.getBody().getExpenseId());
        verify(expenseService, times(1)).addExpense(testRequest);
    }

    @Test
    void addExpense_ServiceThrowsExceptionShouldPropagateException() {
        when(expenseService.addExpense(any(AddExpenseRequest.class)))
            .thenThrow(new IllegalArgumentException("User not in trip"));

        assertThrows(IllegalArgumentException.class,
            () -> expenseController.addExpense(testRequest));

        verify(expenseService, times(1)).addExpense(testRequest);
    }

    @Test
    void updateExpenseValidRequestShouldReturnOkWithExpenseId() {
        ExpenseController.ExpenseRequest updateRequest = new ExpenseController.ExpenseRequest();
        updateRequest.setAmount(new BigDecimal("200.00"));
        
        when(expenseService.updateExpense(1L, updateRequest.getAmount())).thenReturn(testExpense);

        ResponseEntity<ExpenseController.ExpenseResponse> response = 
            expenseController.updateExpense(1L, updateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testExpense.getId(), response.getBody().getExpenseId());
        verify(expenseService, times(1)).updateExpense(1L, updateRequest.getAmount());
    }

    @Test
    void updateExpenseServiceThrowsExceptionShouldPropagateException() {
        ExpenseController.ExpenseRequest updateRequest = new ExpenseController.ExpenseRequest();
        updateRequest.setAmount(new BigDecimal("200.00"));
        
        when(expenseService.updateExpense(anyLong(), any(BigDecimal.class)))
            .thenThrow(new RuntimeException("Expense not found"));

        assertThrows(RuntimeException.class,
            () -> expenseController.updateExpense(999L, updateRequest));

        verify(expenseService, times(1)).updateExpense(999L, updateRequest.getAmount());
    }

    @Test
    void getUserSummaryShouldReturnSummary() {
        Long tripId = 1L;
        Long userId = 1L;
        LocalDate fromDate = LocalDate.of(2025, 1, 1);
        LocalDate toDate = LocalDate.of(2025, 1, 31);
        
        UserExpenseSummary mockSummary = new UserExpenseSummary(
            new BigDecimal("100.00"),
            new BigDecimal("50.00"),
            new BigDecimal("50.00"),
            Map.of(Expense.ExpenseSource.HOTEL, new BigDecimal("50.00"))
        );
        
        when(expenseService.getUserSummary(tripId, userId, fromDate, toDate)).thenReturn(mockSummary);

        ResponseEntity<UserExpenseSummary> response = expenseController.getUserSummary(tripId, userId, fromDate, toDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockSummary, response.getBody());
        verify(expenseService, times(1)).getUserSummary(tripId, userId, fromDate, toDate);
    }

    @Test
    void getBalanceSheetShouldReturnBalanceSheet() {
        Long tripId = 1L;
        BalanceSheet mockBalanceSheet = new BalanceSheet(Map.of(1L, new BigDecimal("50.00")));
        
        when(expenseService.getBalanceSheet(tripId)).thenReturn(mockBalanceSheet);

        ResponseEntity<BalanceSheet> response = expenseController.getBalanceSheet(tripId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockBalanceSheet, response.getBody());
        verify(expenseService, times(1)).getBalanceSheet(tripId);
    }

    private AddExpenseRequest createTestRequest() {
        AddExpenseRequest request = new AddExpenseRequest();
        request.setTripId(1L);
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test expense");
        request.setResponsibleUserIds(Set.of(1L, 2L));
        request.setSource(Expense.ExpenseSource.HOTEL);
        return request;
    }

    private Expense createTestExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setTripId(1L);
        expense.setAmount(new BigDecimal("100.00"));
        expense.setDescription("Test expense");
        expense.setResponsibleUserIds(Set.of(1L, 2L));
        expense.setSource(Expense.ExpenseSource.HOTEL);
        return expense;
    }
}
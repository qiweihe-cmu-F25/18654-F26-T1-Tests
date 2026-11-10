package codenomads.expensemanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.expensemanagement.domain.Expense;
import codenomads.expensemanagement.dto.AddExpenseRequest;
import codenomads.expensemanagement.dto.UserExpenseSummary;
import codenomads.expensemanagement.dto.BalanceSheet;
import codenomads.expensemanagement.gateway.TripGateway;
import codenomads.expensemanagement.repository.ExpenseRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;
    
    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExpenseService expenseService;

    private AddExpenseRequest testRequest;
    private Expense testExpense;

    @BeforeEach
    void setUp() {
        testRequest = createTestRequest();
        testExpense = createTestExpense();
    }

    @Test
    void addExpenseValidRequestShouldSaveExpense() {
        when(tripGateway.isUserInTrip(1L, 1L)).thenReturn(true);
        when(tripGateway.isUserInTrip(1L, 2L)).thenReturn(true);
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        Expense result = expenseService.addExpense(testRequest);

        assertNotNull(result);
        assertEquals(testExpense.getId(), result.getId());
        verify(tripGateway, times(2)).isUserInTrip(anyLong(), anyLong());
        verify(expenseRepository, times(1)).save(any(Expense.class));
    }

    @Test
    void updateExpenseValidIdShouldUpdateAmount() {
        BigDecimal newAmount = new BigDecimal("200.00");
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(testExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(testExpense);

        Expense result = expenseService.updateExpense(1L, newAmount);

        assertNotNull(result);
        verify(expenseRepository, times(1)).findById(1L);
        verify(expenseRepository, times(1)).save(argThat(expense -> 
            expense.getAmount().equals(newAmount)
        ));
    }

    @Test
    void updateExpenseInvalidIdShouldThrowException() {
        when(expenseRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> expenseService.updateExpense(999L, new BigDecimal("200.00")));

        assertEquals("Expense not found", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void addExpenseWhenUserNotInTripShouldThrowCustomException() {
        when(tripGateway.isUserInTrip(1L, 1L)).thenReturn(true);
        when(tripGateway.isUserInTrip(1L, 2L)).thenReturn(false);

        codenomads.expensemanagement.exceptions.CustomException exception = assertThrows(
            codenomads.expensemanagement.exceptions.CustomException.class,
            () -> expenseService.addExpense(testRequest));

        assertEquals(400, exception.getCode());
        assertEquals("User is not a member of the trip", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void updateExpenseNonPositiveAmountShouldThrowCustomException() {
        codenomads.expensemanagement.exceptions.CustomException zeroException = assertThrows(
            codenomads.expensemanagement.exceptions.CustomException.class,
            () -> expenseService.updateExpense(1L, BigDecimal.ZERO));
        assertEquals(400, zeroException.getCode());

        codenomads.expensemanagement.exceptions.CustomException negativeException = assertThrows(
            codenomads.expensemanagement.exceptions.CustomException.class,
            () -> expenseService.updateExpense(1L, new BigDecimal("-10.00")));
        assertEquals(400, negativeException.getCode());

        verify(expenseRepository, never()).findById(anyLong());
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void getUserSummaryValidParametersShouldReturnSummary() {
        Long tripId = 1L;
        Long userId = 1L;
        LocalDate fromDate = LocalDate.of(2025, 1, 1);
        LocalDate toDate = LocalDate.of(2025, 1, 31);
        
        Expense expense1 = createExpenseWithDetails(1L, new BigDecimal("100.00"), userId, Set.of(userId, 2L), Expense.ExpenseSource.HOTEL, fromDate);
        List<Expense> expenses = List.of(expense1);
        
        when(expenseRepository.findByTripIdAndOccurredOnBetween(tripId, fromDate, toDate)).thenReturn(expenses);

        UserExpenseSummary result = expenseService.getUserSummary(tripId, userId, fromDate, toDate);

        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.paidByUser());
        assertEquals(new BigDecimal("50.00"), result.shareOfUser());
        assertEquals(new BigDecimal("50.00"), result.net());
        verify(expenseRepository, times(1)).findByTripIdAndOccurredOnBetween(tripId, fromDate, toDate);
    }

    @Test
    void getUserSummaryNullParametersShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(null, 1L, LocalDate.now(), LocalDate.now().plusDays(1)));
        
        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getUserSummary(1L, null, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @Test
    void getBalanceSheetValidTripIdShouldReturnBalanceSheet() {
        Long tripId = 1L;
        Expense expense1 = createExpenseWithDetails(1L, new BigDecimal("100.00"), 1L, Set.of(1L, 2L), Expense.ExpenseSource.HOTEL, LocalDate.now());
        List<Expense> expenses = List.of(expense1);
        
        when(expenseRepository.findByTripId(tripId)).thenReturn(expenses);

        BalanceSheet result = expenseService.getBalanceSheet(tripId);

        assertNotNull(result);
        assertNotNull(result.netByUser());
        assertTrue(result.netByUser().containsKey(1L));
        verify(expenseRepository, times(1)).findByTripId(tripId);
    }

    @Test
    void getBalanceSheetNullTripIdShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
            () -> expenseService.getBalanceSheet(null));
    }

    private AddExpenseRequest createTestRequest() {
        AddExpenseRequest request = new AddExpenseRequest();
        request.setTripId(1L);
        request.setAmount(new BigDecimal("100.00"));
        request.setDescription("Test expense");
        request.setResponsibleUserIds(Set.of(1L, 2L));
        request.setSource(Expense.ExpenseSource.HOTEL);
        request.setCreatedByUserId(1L);
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

    private Expense createExpenseWithAmount(BigDecimal amount, Set<Long> userIds) {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setTripId(1L);
        expense.setAmount(amount);
        expense.setDescription("Test expense");
        expense.setResponsibleUserIds(userIds);
        expense.setSource(Expense.ExpenseSource.HOTEL);
        return expense;
    }

    private Expense createExpenseWithDetails(Long id, BigDecimal amount, Long createdBy, Set<Long> responsibleUsers, Expense.ExpenseSource source, LocalDate occurredOn) {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setTripId(1L);
        expense.setAmount(amount);
        expense.setDescription("Test expense");
        expense.setCreatedByUserId(createdBy);
        expense.setResponsibleUserIds(responsibleUsers);
        expense.setSource(source);
        expense.setOccurredOn(occurredOn);
        return expense;
    }
}
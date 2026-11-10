package codenomads.expensemanagement.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseTest {

    private Expense expense;

    @BeforeEach
    void setUp() {
        expense = new Expense();
    }

    @Test
    void setAndGetIdShouldWorkCorrectly() {
        Long expectedId = 123L;
        expense.setId(expectedId);
        
        assertEquals(expectedId, expense.getId());
    }

    @Test
    void setAndGetAmountShouldWorkCorrectly() {
        BigDecimal expectedAmount = new BigDecimal("275.50");
        expense.setAmount(expectedAmount);
        
        assertEquals(expectedAmount, expense.getAmount());
    }

    @Test
    void responsibleUserIdsAddUsersShouldMaintainSet() {
        Set<Long> userIds = new HashSet<>();
        userIds.add(1L);
        userIds.add(2L);
        userIds.add(1L); // Duplicate should be ignored
        
        expense.setResponsibleUserIds(userIds);
        
        assertEquals(2, expense.getResponsibleUserIds().size());
        assertTrue(expense.getResponsibleUserIds().contains(1L));
        assertTrue(expense.getResponsibleUserIds().contains(2L));
    }

    @Test
    void expenseSourceAllValuesShouldExist() {
        Expense.ExpenseSource[] sources = Expense.ExpenseSource.values();
        
        assertEquals(4, sources.length);
        assertTrue(Set.of(sources).contains(Expense.ExpenseSource.HOTEL));
        assertTrue(Set.of(sources).contains(Expense.ExpenseSource.FLIGHT));
        assertTrue(Set.of(sources).contains(Expense.ExpenseSource.CARHIRE));
        assertTrue(Set.of(sources).contains(Expense.ExpenseSource.EXPERIENCE));
    }

    @Test
    void bigDecimalAmountPrecisionHandlingShouldMaintainPrecision() {
        BigDecimal preciseAmount = new BigDecimal("123.456789");
        expense.setAmount(preciseAmount);
        
        assertEquals(preciseAmount, expense.getAmount());
        assertEquals(preciseAmount.scale(), expense.getAmount().scale());
    }

    @Test
    void responsibleUserIdsModifyRetrievedSetShouldReflectChanges() {
        expense.setResponsibleUserIds(new HashSet<>(Set.of(1L, 2L)));
        
        Set<Long> userIds = expense.getResponsibleUserIds();
        userIds.add(3L);
        
        assertTrue(expense.getResponsibleUserIds().contains(3L));
        assertEquals(3, expense.getResponsibleUserIds().size());
    }
}
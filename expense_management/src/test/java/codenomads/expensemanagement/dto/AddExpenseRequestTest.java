package codenomads.expensemanagement.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import codenomads.expensemanagement.domain.Expense;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class AddExpenseRequestTest {

    private AddExpenseRequest addExpenseRequest;

    @BeforeEach
    void setUp() {
        addExpenseRequest = new AddExpenseRequest();
    }

    @Test
    void setAndGetTripIdShouldWorkCorrectly() {
        Long expectedTripId = 123L;
        addExpenseRequest.setTripId(expectedTripId);

        assertEquals(expectedTripId, addExpenseRequest.getTripId());
    }

    @Test
    void setAndGetAmountShouldWorkCorrectly() {
        BigDecimal expectedAmount = new BigDecimal("250.75");
        addExpenseRequest.setAmount(expectedAmount);

        assertEquals(expectedAmount, addExpenseRequest.getAmount());
    }

    @Test
    void setAndGetDescriptionShouldWorkCorrectly() {
        String expectedDescription = "Team lunch at conference";
        addExpenseRequest.setDescription(expectedDescription);

        assertEquals(expectedDescription, addExpenseRequest.getDescription());
    }

    @Test
    void setAndGetSourceShouldWorkCorrectly() {
        Expense.ExpenseSource expectedSource = Expense.ExpenseSource.CARHIRE;
        addExpenseRequest.setSource(expectedSource);

        assertEquals(expectedSource, addExpenseRequest.getSource());
    }

    @Test
    void setAndGetNegativeAmountShouldStoreValue() {
        BigDecimal negativeAmount = new BigDecimal("-50.00");
        addExpenseRequest.setAmount(negativeAmount);

        assertEquals(negativeAmount, addExpenseRequest.getAmount());
        assertTrue(addExpenseRequest.getAmount().compareTo(BigDecimal.ZERO) < 0);
    }
}
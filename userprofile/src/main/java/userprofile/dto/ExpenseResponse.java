package userprofile.dto;
import userprofile.model.Expense;

import java.util.List;

public class ExpenseResponse {
    private List<Expense> expenses;

    public ExpenseResponse() { }

    public ExpenseResponse(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
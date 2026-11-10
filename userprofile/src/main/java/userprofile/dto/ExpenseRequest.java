package userprofile.dto;

public class ExpenseRequest {
    private Long userId;

    public ExpenseRequest(Long userId) {
        this.userId = userId;
    }

    public ExpenseRequest() { }

    public Long getUserId() {
        return userId;
    }
}
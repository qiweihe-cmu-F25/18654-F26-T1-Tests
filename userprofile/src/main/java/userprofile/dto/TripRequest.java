package userprofile.dto;

public class TripRequest {
    private Long userId;

    public TripRequest(Long userId) {
        this.userId = userId;
    }

    public TripRequest() { }

    public Long getUserId() {
        return userId;
    }
}

package codenomads.tripmanagement.dto;

import codenomads.tripmanagement.domain.TripMember;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddTripMemberRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    @NotNull(message = "Trip member role is required")
    private TripMember.Role role;
}

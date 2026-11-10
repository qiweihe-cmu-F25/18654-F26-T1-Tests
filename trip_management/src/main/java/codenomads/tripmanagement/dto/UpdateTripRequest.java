package codenomads.tripmanagement.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTripRequest {

    @NotNull(message = "Trip name is required")
    private String tripName;

    @NotNull(message = "Trip's start date is required")
    private Date startDate;

    @NotNull(message = "Trip's end date is required")
    private Date endDate;

    // Optional. Accepted for request compatibility; membership changes use /members endpoints.
    private List<Long> members;
}

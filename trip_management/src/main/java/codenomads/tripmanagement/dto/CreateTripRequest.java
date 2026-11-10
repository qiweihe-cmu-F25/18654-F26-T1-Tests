package codenomads.tripmanagement.dto;

import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateTripRequest {

    @NotNull(message = "Trip name is required")
    private String tripName;

    @NotNull(message = "Trip's start date is required")
    private Date startDate;

    @NotNull(message = "Trip's end date is required")
    private Date endDate;

    @NotEmpty(message = "At least one trip member is required")
    private List<Long> members;
}

package codenomads.carhire_search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import lombok.Data;
import java.util.Date;

@Data
public class CarhireSearchCriteria {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    private Double maxPrice;
    private Double minPrice;
    private String type;

    @Min(value = 1, message = "num_passengers must be a positive integer")
    private Integer numPassengers;

    // Ignored by Jackson so it isn't sent to the provider as an extra field
    @JsonIgnore
    @AssertTrue(message = "end_date must not be before start_date")
    public boolean isEndDateValid() {
        return startDate == null || endDate == null || !endDate.before(startDate);
    }
}
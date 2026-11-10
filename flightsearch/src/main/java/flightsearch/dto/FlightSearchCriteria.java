package flightsearch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlightSearchCriteria {

    private String departureAirport;
    private String arrivalAirport;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date departureDatetime;

    private String airline;
    private Double maxPrice;
    private Double minPrice;
    private Integer numPassengers;
    private String flightId;
}
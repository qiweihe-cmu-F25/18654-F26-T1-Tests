package codenomads.flight_tracking.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import java.util.Set;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightBookingDTO {
    @NotNull(message = "data is required")
    @Valid
    private FlightData data;
    private BigDecimal amountPaid;
    private Long tripId;
    private Set<Long> responsibleUserIds;

    @Data
    public static class FlightData {
        @Nullable
        private UUID bookingId;
        @NotNull(message = "data.flight_id is required")
        private UUID flightId;
        @NotBlank(message = "data.carrier is required")
        private String carrier;
        @NotBlank(message = "data.flight_number is required")
        private String flightNumber;
        @NotBlank(message = "data.origin is required")
        private String origin;
        @NotBlank(message = "data.destination is required")
        private String destination;
        @NotNull(message = "data.distance_miles is required")
        private Integer distanceMiles;
        @NotNull(message = "data.total_seats is required")
        private Integer totalSeats;
        @NotNull(message = "data.available_seats is required")
        private Integer availableSeats;
        @NotBlank(message = "data.aircraft is required")
        private String aircraft;
        @NotNull(message = "data.airtime_minutes is required")
        @JsonProperty("airtime_minutes")
        private Integer airtime;
        @NotNull(message = "data.departure_datetime is required")
        @JsonProperty("departure_datetime")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date departureDateTime;
        @NotNull(message = "data.flight_price is required")
        @JsonProperty("flight_price")
        private BigDecimal flightPrice;
    }
}
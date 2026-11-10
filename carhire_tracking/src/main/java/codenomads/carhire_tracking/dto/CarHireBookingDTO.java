package codenomads.carhire_tracking.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CarHireBookingDTO {
    @NotNull(message = "data is required")
    @Valid
    private CarHireData data;
    private BigDecimal amountPaid;
    private Long tripId;
    private Set<Long> responsibleUserIds;  // Add this line

    @Data
    public static class CarHireData {
        @NotNull(message = "carhire is required")
        @Valid
        private CarHireDTO carhire;
        @NotNull(message = "booking is required")
        @Valid
        private BookingDTO booking;
    }

    @Data
    public static class CarHireDTO {
        private String operator;
        @JsonProperty("license_plate")
        private String licensePlate;
        private String vin;
        @JsonProperty("vehicle_year")
        private int vehicleYear;
        private String phone;
        private String website;
        private String id;
        @JsonProperty("daily_rate")
        private BigDecimal dailyRate;
        @JsonProperty("vehicle_type")
        private String vehicleType;
        @JsonProperty("seat_capacity")
        private int seatCapacity;
    }

    @Data
    public static class BookingDTO {
        @NotBlank(message = "booking.carhire_id is required")
        @JsonProperty("carhire_id")
        private String carHireId;
        @NotBlank(message = "booking.id is required")
        private String id;
        @NotNull(message = "start_date is required")
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date startDate;
        @NotNull(message = "end_date is required")
        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date endDate;
    }
}
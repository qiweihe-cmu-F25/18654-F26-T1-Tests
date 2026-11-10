package codenomads.experience_tracking.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExperienceBookingDTO {
    @NotNull(message = "booking is required")
    @Valid
    private BookingDTO booking;
    @NotNull(message = "experience is required")
    @Valid
    private ExperienceDTO experience;
    private BigDecimal amountPaid; // Optional field for amount paid
    private Long tripId;           // Optional field for trip ID
    private Set<Long> responsibleUserIds;

    @Data
    public static class BookingDTO {
        @NotBlank(message = "booking.id is required")
        private String id;
        private String experienceId;

        @NotNull(message = "booking.startDate is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date startDate;
        private int participants;
    }

    @Data
    public static class ExperienceDTO {
        private String id;
        private String organizer;
        private String eventName;
        private String address;
        private String city;
        private String state;
        private int postcode;
        private String borough;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String nta;
        private int capacity;
        private BigDecimal pricePerPerson;
    }
}
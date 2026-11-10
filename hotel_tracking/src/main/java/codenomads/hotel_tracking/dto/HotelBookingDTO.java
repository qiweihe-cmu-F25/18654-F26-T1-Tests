package codenomads.hotel_tracking.dto;

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
public class HotelBookingDTO {
    @NotNull(message = "data is required")
    @Valid
    private HotelData data;
    private BigDecimal amountPaid;
    private Long tripId;
    private Set<Long> responsibleUserIds;

    @Data
    public static class HotelData {
        @NotNull(message = "data.hotel is required")
        @Valid
        private HotelDTO hotel;
        @NotNull(message = "data.booking is required")
        @Valid
        private BookingDTO booking;
    }

    @Data
    public static class HotelDTO {
        @NotBlank(message = "data.hotel.hotel_id is required")
        @JsonProperty("hotel_id")
        private String hotelId;
        @JsonProperty("borough_code")
        private Integer borocode;
        private Integer block;
        private Integer lot;
        @JsonProperty("street_number")
        private String streetNumber;
        @JsonProperty("street_name")
        private String streetName;
        private Integer zipcode;
        @JsonProperty("building_class")
        private String buildingClass;
        @JsonProperty("owner_name")
        private String ownerName;
        private String borough;
        private String latitude;
        private String longitude;
        private String area;
    }

    @Data
    public static class BookingDTO {
        @JsonProperty("hotel_id")
        private String hotelId;
        @NotBlank(message = "data.booking.id is required")
        private String id;
        @NotNull(message = "data.booking.start_date is required")
        @JsonProperty("start_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date startDate;
        @NotNull(message = "data.booking.end_date is required")
        @JsonProperty("end_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Date endDate;
        @JsonProperty("one_person_rooms")
        private Integer onePersonRooms;
        @JsonProperty("two_person_rooms")
        private Integer twoPersonRooms;
        @JsonProperty("four_person_rooms")
        private Integer fourPersonRooms;
    }
}
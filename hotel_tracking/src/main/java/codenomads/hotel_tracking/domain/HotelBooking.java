package codenomads.hotel_tracking.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "hotel_booking")
@Data
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false, unique = true)
    private String bookingId;

    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "external_expense_reference")
    private Long externalExpenseReference;

    // Hotel details
    @Column(name = "borocode")
    private Integer borocode;

    @Column(name = "block")
    private Integer block;

    @Column(name = "lot")
    private Integer lot;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "zipcode")
    private Integer zipcode;

    @Column(name = "building_class")
    private String buildingClass;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "borough")
    private String borough;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "area")
    private String area;

    // Booking details
    @Column(name = "one_person_rooms")
    private Integer onePersonRooms;

    @Column(name = "two_person_rooms")
    private Integer twoPersonRooms;

    @Column(name = "four_person_rooms")
    private Integer fourPersonRooms;
}
package codenomads.experience_tracking.domain;

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
@Table(name = "experience_booking")
@Data
public class ExperienceBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false, unique = true)
    private String bookingId;

    @Column(name = "experience_id", nullable = false)
    private String experienceId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "participants")
    private int participants;

    @Column(name = "external_expense_reference")
    private Long externalExpenseReference;

    // Experience details
    @Column(name = "organizer")
    private String organizer;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postcode")
    private int postcode;

    @Column(name = "borough")
    private String borough;

    @Column(name = "latitude", precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "nta")
    private String nta;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "price_per_person", precision = 10, scale = 2)
    private BigDecimal pricePerPerson;
}
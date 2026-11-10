package codenomads.flight_tracking.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "flight_booking")
@Data
public class FlightBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", nullable = false, unique = true)
    private UUID bookingId;

    @Column(name = "flight_id", nullable = false)
    private UUID flightId;

    @Column(name = "carrier", nullable = false)
    private String carrier;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "distance_miles", nullable = false)
    private Integer distanceMiles;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "aircraft", nullable = false)
    private String aircraft;

    @Column(name = "airtime", nullable = false)
    private Integer airtime;

    @Column(name = "departure_datetime", nullable = false)
    private Date departureDateTime;

    @Column(name = "flight_price", nullable = false)
    private BigDecimal flightPrice;

    @Column(name = "external_expense_reference")
    private Long externalExpenseReference;
}
package codenomads.flight_tracking.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightBookingTest {

    private FlightBooking flightBooking;
    private UUID testBookingId;
    private UUID testFlightId;
    private Date testDepartureDateTime;

    @BeforeEach
    void setUp() {
        flightBooking = new FlightBooking();
        testBookingId = UUID.randomUUID();
        testFlightId = UUID.randomUUID();
        testDepartureDateTime = new Date();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        Long expectedId = 123L;
        flightBooking.setId(expectedId);
        
        assertEquals(expectedId, flightBooking.getId());
    }

    @Test
    void setAndGetBookingId_ShouldWorkCorrectly() {
        flightBooking.setBookingId(testBookingId);
        
        assertEquals(testBookingId, flightBooking.getBookingId());
    }

    @Test
    void setAndGetFlightId_ShouldWorkCorrectly() {
        flightBooking.setFlightId(testFlightId);
        
        assertEquals(testFlightId, flightBooking.getFlightId());
    }

    @Test
    void setAndGetCarrier_ShouldWorkCorrectly() {
        String expectedCarrier = "United Airlines";
        flightBooking.setCarrier(expectedCarrier);
        
        assertEquals(expectedCarrier, flightBooking.getCarrier());
    }

    @Test
    void setAndGetFlightNumber_ShouldWorkCorrectly() {
        String expectedFlightNumber = "UA2468";
        flightBooking.setFlightNumber(expectedFlightNumber);
        
        assertEquals(expectedFlightNumber, flightBooking.getFlightNumber());
    }

    @Test
    void setAndGetOrigin_ShouldWorkCorrectly() {
        String expectedOrigin = "SFO";
        flightBooking.setOrigin(expectedOrigin);
        
        assertEquals(expectedOrigin, flightBooking.getOrigin());
    }

    @Test
    void setAndGetDestination_ShouldWorkCorrectly() {
        String expectedDestination = "DEN";
        flightBooking.setDestination(expectedDestination);
        
        assertEquals(expectedDestination, flightBooking.getDestination());
    }

    @Test
    void setAndGetDistanceMiles_ShouldWorkCorrectly() {
        Integer expectedDistanceMiles = 967;
        flightBooking.setDistanceMiles(expectedDistanceMiles);
        
        assertEquals(expectedDistanceMiles, flightBooking.getDistanceMiles());
    }
}
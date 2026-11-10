package codenomads.flight_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightBookingDTOTest {

    private FlightBookingDTO flightBookingDTO;
    private FlightBookingDTO.FlightData flightData;

    @BeforeEach
    void setUp() {
        flightBookingDTO = new FlightBookingDTO();
        flightData = new FlightBookingDTO.FlightData();
    }


    @Test
    void flightDataSetAndGetFlightNumberShouldWorkCorrectly() {
        String expectedFlightNumber = "B6123";
        flightData.setFlightNumber(expectedFlightNumber);
        
        assertEquals(expectedFlightNumber, flightData.getFlightNumber());
    }

    @Test
    void flightDataSetAndGetOriginShouldWorkCorrectly() {
        String expectedOrigin = "BOS";
        flightData.setOrigin(expectedOrigin);
        
        assertEquals(expectedOrigin, flightData.getOrigin());
    }

    @Test
    void flightDataSetAndGetDestinationShouldWorkCorrectly() {
        String expectedDestination = "SEA";
        flightData.setDestination(expectedDestination);
        
        assertEquals(expectedDestination, flightData.getDestination());
    }

    @Test
    void flightDataSetAndGetDistanceMilesShouldWorkCorrectly() {
        Integer expectedDistanceMiles = 2496;
        flightData.setDistanceMiles(expectedDistanceMiles);
        
        assertEquals(expectedDistanceMiles, flightData.getDistanceMiles());
    }

    @Test
    void flightDataNullBookingIdShouldBeAllowed() {
        flightData.setBookingId(null);
        
        assertNull(flightData.getBookingId());
    }

    @Test
    void responsibleUserIdsEmptySetShouldBeAllowed() {
        flightBookingDTO.setResponsibleUserIds(Set.of());
        
        assertEquals(0, flightBookingDTO.getResponsibleUserIds().size());
        assertTrue(flightBookingDTO.getResponsibleUserIds().isEmpty());
    }
}
package codenomads.flight_tracking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codenomads.flight_tracking.domain.FlightBooking;
import codenomads.flight_tracking.dto.FlightBookingDTO;
import codenomads.flight_tracking.service.FlightTrackingService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightTrackingControllerTest {

    @Mock
    private FlightTrackingService flightTrackingService;

    @InjectMocks
    private FlightTrackingController flightTrackingController;

    private FlightBookingDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
    }

    @Test
    void healthCheckShouldReturnOkResponse() {
        ResponseEntity<String> response = flightTrackingController.healthCheck();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Flight tracking service is up and running", response.getBody());
    }

    @Test
    void receiveFlightBookingValidDTOShouldReturnOkResponse() {
        FlightBooking mockBooking = createMockBooking();
        when(flightTrackingService.saveFlightBooking(any(FlightBookingDTO.class))).thenReturn(mockBooking);

        ResponseEntity<String> response = flightTrackingController.receiveFlightBooking(testDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Flight booking received", response.getBody());
        verify(flightTrackingService, times(1)).saveFlightBooking(testDTO);
    }

    @Test
    void receiveFlightBookingServiceThrowsExceptionShouldPropagateException() {
        when(flightTrackingService.saveFlightBooking(any(FlightBookingDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid flight data"));

        assertThrows(IllegalArgumentException.class, 
            () -> flightTrackingController.receiveFlightBooking(testDTO));
        
        verify(flightTrackingService, times(1)).saveFlightBooking(testDTO);
    }

    @Test
    void receiveFlightBookingRuntimeExceptionShouldPropagateException() {
        when(flightTrackingService.saveFlightBooking(any(FlightBookingDTO.class)))
            .thenThrow(new RuntimeException("Database connection error"));

        assertThrows(RuntimeException.class, 
            () -> flightTrackingController.receiveFlightBooking(testDTO));
        
        verify(flightTrackingService, times(1)).saveFlightBooking(testDTO);
    }

    private FlightBookingDTO createTestDTO() {
        FlightBookingDTO dto = new FlightBookingDTO();
        
        FlightBookingDTO.FlightData data = new FlightBookingDTO.FlightData();
        data.setBookingId(UUID.randomUUID());
        data.setFlightId(UUID.randomUUID());
        data.setCarrier("Delta Airlines");
        data.setFlightNumber("DL567");
        data.setOrigin("ATL");
        data.setDestination("ORD");
        data.setDistanceMiles(606);
        data.setTotalSeats(160);
        data.setAvailableSeats(32);
        data.setAircraft("Airbus A320");
        data.setAirtime(120);
        data.setDepartureDateTime(new Date());
        data.setFlightPrice(new BigDecimal("189.50"));
        
        dto.setData(data);
        
        return dto;
    }

    private FlightBooking createMockBooking() {
        FlightBooking booking = new FlightBooking();
        booking.setId(1L);
        booking.setBookingId(UUID.randomUUID());
        booking.setFlightId(UUID.randomUUID());
        booking.setCarrier("Delta Airlines");
        booking.setFlightNumber("DL567");
        return booking;
    }
}
package codenomads.carhire_tracking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import codenomads.carhire_tracking.domain.CarHireBooking;
import codenomads.carhire_tracking.dto.CarHireBookingDTO;
import codenomads.carhire_tracking.service.CarHireTrackingService;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarHireTrackingControllerTest {

    @Mock
    private CarHireTrackingService carHireTrackingService;

    @InjectMocks
    private CarHireTrackingController carHireTrackingController;

    private CarHireBookingDTO testDTO;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
        objectMapper = new ObjectMapper();
    }

    @Test
    void healthCheck_ShouldReturnOkResponse() {
        ResponseEntity<String> response = carHireTrackingController.healthCheck();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car hire tracking service is up and running", response.getBody());
    }

    @Test
    void receiveCarHireBooking_ValidDTO_ShouldReturnOkResponse() {
        CarHireBooking mockBooking = createMockBooking();
        when(carHireTrackingService.saveCarHireBooking(any(CarHireBookingDTO.class))).thenReturn(mockBooking);

        ResponseEntity<String> response = carHireTrackingController.receiveCarHireBooking(testDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car hire booking received", response.getBody());
        verify(carHireTrackingService, times(1)).saveCarHireBooking(testDTO);
    }

    @Test
    void receiveCarHireBooking_ServiceThrowsException_ShouldPropagateException() {
        when(carHireTrackingService.saveCarHireBooking(any(CarHireBookingDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid booking data"));

        assertThrows(IllegalArgumentException.class, 
            () -> carHireTrackingController.receiveCarHireBooking(testDTO));
        
        verify(carHireTrackingService, times(1)).saveCarHireBooking(testDTO);
    }

    @Test
    void receiveCarHireBooking_NullDTO_ShouldHandleGracefully() {
        CarHireBooking mockBooking = createMockBooking();
        when(carHireTrackingService.saveCarHireBooking(null)).thenReturn(mockBooking);

        ResponseEntity<String> response = carHireTrackingController.receiveCarHireBooking(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Car hire booking received", response.getBody());
        verify(carHireTrackingService, times(1)).saveCarHireBooking(null);
    }

    private CarHireBookingDTO createTestDTO() {
        CarHireBookingDTO dto = new CarHireBookingDTO();
        
        CarHireBookingDTO.CarHireData data = new CarHireBookingDTO.CarHireData();
        
        CarHireBookingDTO.CarHireDTO carhire = new CarHireBookingDTO.CarHireDTO();
        carhire.setOperator("Test Operator");
        carhire.setLicensePlate("ABC123");
        carhire.setVin("1HGCM82633A123456");
        carhire.setVehicleYear(2023);
        carhire.setPhone("555-1234");
        carhire.setWebsite("www.test.com");
        carhire.setDailyRate(new BigDecimal("50.00"));
        carhire.setVehicleType("SUV");
        carhire.setSeatCapacity(5);
        
        CarHireBookingDTO.BookingDTO booking = new CarHireBookingDTO.BookingDTO();
        booking.setId("booking-123");
        booking.setCarHireId("car-456");
        booking.setStartDate(new Date());
        booking.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        
        data.setCarhire(carhire);
        data.setBooking(booking);
        dto.setData(data);
        
        return dto;
    }

    private CarHireBooking createMockBooking() {
        CarHireBooking booking = new CarHireBooking();
        booking.setId(1L);
        booking.setBookingId("booking-123");
        booking.setCarHireId("car-456");
        return booking;
    }
}
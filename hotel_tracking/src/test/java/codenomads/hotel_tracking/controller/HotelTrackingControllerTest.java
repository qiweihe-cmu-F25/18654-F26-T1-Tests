package codenomads.hotel_tracking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codenomads.hotel_tracking.domain.HotelBooking;
import codenomads.hotel_tracking.dto.HotelBookingDTO;
import codenomads.hotel_tracking.service.HotelTrackingService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelTrackingControllerTest {

    @Mock
    private HotelTrackingService hotelTrackingService;

    @InjectMocks
    private HotelTrackingController hotelTrackingController;

    private HotelBookingDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
    }

    @Test
    void healthCheckShouldReturnOkResponse() {
        ResponseEntity<String> response = hotelTrackingController.healthCheck();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel tracking service is up and running", response.getBody());
    }

    @Test
    void receiveHotelBookingValidDTOShouldReturnOkResponse() {
        HotelBooking mockBooking = createMockBooking();
        when(hotelTrackingService.saveHotelBooking(any(HotelBookingDTO.class))).thenReturn(mockBooking);

        ResponseEntity<String> response = hotelTrackingController.receiveHotelBooking(testDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotel booking received", response.getBody());
        verify(hotelTrackingService, times(1)).saveHotelBooking(testDTO);
    }

    @Test
    void receiveHotelBookingServiceThrowsExceptionShouldPropagateException() {
        when(hotelTrackingService.saveHotelBooking(any(HotelBookingDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid hotel data"));

        assertThrows(IllegalArgumentException.class, 
            () -> hotelTrackingController.receiveHotelBooking(testDTO));
        
        verify(hotelTrackingService, times(1)).saveHotelBooking(testDTO);
    }

    @Test
    void receiveHotelBookingRuntimeExceptionShouldPropagateException() {
        when(hotelTrackingService.saveHotelBooking(any(HotelBookingDTO.class)))
            .thenThrow(new RuntimeException("Database connection error"));

        assertThrows(RuntimeException.class, 
            () -> hotelTrackingController.receiveHotelBooking(testDTO));
        
        verify(hotelTrackingService, times(1)).saveHotelBooking(testDTO);
    }

    private HotelBookingDTO createTestDTO() {
        HotelBookingDTO dto = new HotelBookingDTO();
        
        HotelBookingDTO.HotelData data = new HotelBookingDTO.HotelData();
        
        HotelBookingDTO.HotelDTO hotel = new HotelBookingDTO.HotelDTO();
        hotel.setHotelId("hotel-555");
        hotel.setBorocode(3);
        hotel.setBlock(1234);
        hotel.setLot(5678);
        hotel.setStreetNumber("456");
        hotel.setStreetName("Park Avenue");
        hotel.setZipcode(10022);
        hotel.setBuildingClass("R9");
        hotel.setOwnerName("Hilton Worldwide");
        hotel.setBorough("Manhattan");
        hotel.setLatitude("40.7614");
        hotel.setLongitude("-73.9776");
        hotel.setArea("Midtown East");
        
        HotelBookingDTO.BookingDTO booking = new HotelBookingDTO.BookingDTO();
        booking.setId("booking-hotel-777");
        booking.setHotelId("hotel-555");
        booking.setStartDate(new Date());
        booking.setEndDate(new Date(System.currentTimeMillis() + 86400000 * 2)); // 2 days later
        booking.setOnePersonRooms(0);
        booking.setTwoPersonRooms(1);
        booking.setFourPersonRooms(1);
        
        data.setHotel(hotel);
        data.setBooking(booking);
        dto.setData(data);
        
        return dto;
    }

    private HotelBooking createMockBooking() {
        HotelBooking booking = new HotelBooking();
        booking.setId(1L);
        booking.setBookingId("booking-hotel-777");
        booking.setHotelId("hotel-555");
        booking.setBorough("Manhattan");
        booking.setOwnerName("Hilton Worldwide");
        return booking;
    }
}
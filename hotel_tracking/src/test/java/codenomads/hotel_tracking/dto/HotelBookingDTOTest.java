package codenomads.hotel_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HotelBookingDTOTest {

    private HotelBookingDTO hotelBookingDTO;
    private HotelBookingDTO.HotelData hotelData;
    private HotelBookingDTO.HotelDTO hotelDTO;
    private HotelBookingDTO.BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        hotelBookingDTO = new HotelBookingDTO();
        hotelData = new HotelBookingDTO.HotelData();
        hotelDTO = new HotelBookingDTO.HotelDTO();
        bookingDTO = new HotelBookingDTO.BookingDTO();
    }

    @Test
    void setAndGetData_ShouldWorkCorrectly() {
        hotelBookingDTO.setData(hotelData);
        
        assertEquals(hotelData, hotelBookingDTO.getData());
    }

    @Test
    void setAndGetAmountPaid_ShouldWorkCorrectly() {
        BigDecimal expectedAmount = new BigDecimal("275.00");
        hotelBookingDTO.setAmountPaid(expectedAmount);
        
        assertEquals(expectedAmount, hotelBookingDTO.getAmountPaid());
    }

    @Test
    void setAndGetTripId_ShouldWorkCorrectly() {
        Long expectedTripId = 777L;
        hotelBookingDTO.setTripId(expectedTripId);
        
        assertEquals(expectedTripId, hotelBookingDTO.getTripId());
    }

    @Test
    void hotelDTO_setAndGetStreetNumber_ShouldWorkCorrectly() {
        String expectedStreetNumber = "567";
        hotelDTO.setStreetNumber(expectedStreetNumber);
        
        assertEquals(expectedStreetNumber, hotelDTO.getStreetNumber());
    }


    @Test
    void dateRangeShouldBeValid() {
        Date checkIn = new Date();
        Date checkOut = new Date(checkIn.getTime() + 86400000 * 7); // 7 days later
        
        bookingDTO.setStartDate(checkIn);
        bookingDTO.setEndDate(checkOut);
        
        assertEquals(checkIn, bookingDTO.getStartDate());
        assertEquals(checkOut, bookingDTO.getEndDate());
        assertTrue(bookingDTO.getEndDate().after(bookingDTO.getStartDate()));
    }
}
package codenomads.hotel_tracking.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class HotelBookingTest {

    private HotelBooking hotelBooking;
    private Date testStartDate;
    private Date testEndDate;

    @BeforeEach
    void setUp() {
        hotelBooking = new HotelBooking();
        testStartDate = new Date();
        testEndDate = new Date(System.currentTimeMillis() + 86400000);
    }

    @Test
    void setAndGetIdShouldWorkCorrectly() {
        Long expectedId = 456L;
        hotelBooking.setId(expectedId);
        
        assertEquals(expectedId, hotelBooking.getId());
    }

    @Test
    void setAndGetBookingIdShouldWorkCorrectly() {
        String expectedBookingId = "hotel-booking-123";
        hotelBooking.setBookingId(expectedBookingId);
        
        assertEquals(expectedBookingId, hotelBooking.getBookingId());
    }

    @Test
    void setAndGetHotelIdShouldWorkCorrectly() {
        String expectedHotelId = "hotel-456";
        hotelBooking.setHotelId(expectedHotelId);
        
        assertEquals(expectedHotelId, hotelBooking.getHotelId());
    }

    @Test
    void zeroRoomBookingShouldBeValid() {
        hotelBooking.setOnePersonRooms(0);
        hotelBooking.setTwoPersonRooms(0);
        hotelBooking.setFourPersonRooms(0);
        
        assertEquals(0, hotelBooking.getOnePersonRooms());
        assertEquals(0, hotelBooking.getTwoPersonRooms());
        assertEquals(0, hotelBooking.getFourPersonRooms());
    }
}
package codenomads.carhire_tracking.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CarHireBookingTest {

    private CarHireBooking carHireBooking;
    private Date testStartDate;
    private Date testEndDate;

    @BeforeEach
    void setUp() {
        carHireBooking = new CarHireBooking();
        testStartDate = new Date();
        testEndDate = new Date(System.currentTimeMillis() + 86400000);
    }

    @Test
    void setAndGetIdShouldWorkCorrectly() {
        Long expectedId = 123L;
        carHireBooking.setId(expectedId);
        
        assertEquals(expectedId, carHireBooking.getId());
    }

    @Test
    void setAndGetBookingIdShouldWorkCorrectly() {
        String expectedBookingId = "booking-abc123";
        carHireBooking.setBookingId(expectedBookingId);
        
        assertEquals(expectedBookingId, carHireBooking.getBookingId());
    }

    @Test
    void setAndGetCarHireIdShouldWorkCorrectly() {
        String expectedCarHireId = "carhire-xyz789";
        carHireBooking.setCarHireId(expectedCarHireId);
        
        assertEquals(expectedCarHireId, carHireBooking.getCarHireId());
    }

    @Test
    void setAndGetDatesShouldWorkCorrectly() {
        carHireBooking.setStartDate(testStartDate);
        carHireBooking.setEndDate(testEndDate);
        
        assertEquals(testStartDate, carHireBooking.getStartDate());
        assertEquals(testEndDate, carHireBooking.getEndDate());
    }

    @Test
    void setAndGetOperatorShouldWorkCorrectly() {
        String expectedOperator = "Enterprise Rent-A-Car";
        carHireBooking.setOperator(expectedOperator);
        
        assertEquals(expectedOperator, carHireBooking.getOperator());
    }


}
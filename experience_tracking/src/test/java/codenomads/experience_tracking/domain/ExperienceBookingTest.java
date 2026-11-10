package codenomads.experience_tracking.domain;

import codenomads.experience_tracking.domain.ExperienceBooking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ExperienceBookingTest {

    private ExperienceBooking experienceBooking;
    private Date testStartDate;

    @BeforeEach
    void setUp() {
        experienceBooking = new ExperienceBooking();
        testStartDate = new Date();
    }

    @Test
    void setAndGetIdShouldWorkCorrectly() {
        Long expectedId = 789L;
        experienceBooking.setId(expectedId);
        
        assertEquals(expectedId, experienceBooking.getId());
    }

    @Test
    void setAndGetBookingIdShouldWorkCorrectly() {
        String expectedBookingId = "exp-booking-123";
        experienceBooking.setBookingId(expectedBookingId);
        
        assertEquals(expectedBookingId, experienceBooking.getBookingId());
    }

    @Test
    void setAndGetExperienceIdShouldWorkCorrectly() {
        String expectedExperienceId = "experience-456";
        experienceBooking.setExperienceId(expectedExperienceId);

        assertEquals(expectedExperienceId, experienceBooking.getExperienceId());
    }

    @Test
    void setAndGetStartDateShouldWorkCorrectly() {
        experienceBooking.setStartDate(testStartDate);

        assertEquals(testStartDate, experienceBooking.getStartDate());
    }

    @Test
    void shouldAllowNullStartDate() {
        ExperienceBooking booking = new ExperienceBooking();
        booking.setStartDate(null);

        assertNull(booking.getStartDate());
    }

    @Test
    void setAllLocationFields_ShouldWorkCorrectly() {
        experienceBooking.setAddress("123 Main St");
        experienceBooking.setCity("Brooklyn");
        experienceBooking.setState("NY");
        experienceBooking.setPostcode(11201);
        experienceBooking.setBorough("Brooklyn");
        experienceBooking.setLatitude(new BigDecimal("40.6962"));
        experienceBooking.setLongitude(new BigDecimal("-73.9961"));
        experienceBooking.setNta("DUMBO-Vinegar Hill");
        
        assertEquals("123 Main St", experienceBooking.getAddress());
        assertEquals("Brooklyn", experienceBooking.getCity());
        assertEquals("NY", experienceBooking.getState());
        assertEquals(11201, experienceBooking.getPostcode());
        assertEquals("Brooklyn", experienceBooking.getBorough());
        assertEquals(new BigDecimal("40.6962"), experienceBooking.getLatitude());
        assertEquals(new BigDecimal("-73.9961"), experienceBooking.getLongitude());
        assertEquals("DUMBO-Vinegar Hill", experienceBooking.getNta());
    }
}
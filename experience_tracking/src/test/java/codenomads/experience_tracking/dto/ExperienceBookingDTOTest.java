package codenomads.experience_tracking.dto;

import codenomads.experience_tracking.dto.ExperienceBookingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExperienceBookingDTOTest {

    private ExperienceBookingDTO experienceBookingDTO;
    private ExperienceBookingDTO.BookingDTO bookingDTO;
    private ExperienceBookingDTO.ExperienceDTO experienceDTO;

    @BeforeEach
    void setUp() {
        experienceBookingDTO = new ExperienceBookingDTO();
        bookingDTO = new ExperienceBookingDTO.BookingDTO();
        experienceDTO = new ExperienceBookingDTO.ExperienceDTO();
    }

    @Test
    void setAndGetBookingShouldWorkCorrectly() {
        experienceBookingDTO.setBooking(bookingDTO);
        
        assertEquals(bookingDTO, experienceBookingDTO.getBooking());
    }

    @Test
    void setAndGetExperienceShouldWorkCorrectly() {
        experienceBookingDTO.setExperience(experienceDTO);
        
        assertEquals(experienceDTO, experienceBookingDTO.getExperience());
    }

    @Test
    void setAndGetAmountPaidShouldWorkCorrectly() {
        BigDecimal expectedAmount = new BigDecimal("199.99");
        experienceBookingDTO.setAmountPaid(expectedAmount);
        
        assertEquals(expectedAmount, experienceBookingDTO.getAmountPaid());
    }

    @Test
    void setAndGetTripIdShouldWorkCorrectly() {
        Long expectedTripId = 888L;
        experienceBookingDTO.setTripId(expectedTripId);
        
        assertEquals(expectedTripId, experienceBookingDTO.getTripId());
    }

    @Test
    void experienceDTOSetAndGetCapacityShouldWorkCorrectly() {
        int expectedCapacity = 30;
        experienceDTO.setCapacity(expectedCapacity);
        
        assertEquals(expectedCapacity, experienceDTO.getCapacity());
    }

    @Test
    void experienceDTOSetAndGetPricePerPersonShouldWorkCorrectly() {
        BigDecimal expectedPricePerPerson = new BigDecimal("125.00");
        experienceDTO.setPricePerPerson(expectedPricePerPerson);
        
        assertEquals(expectedPricePerPerson, experienceDTO.getPricePerPerson());
    }

    @Test
    void completeDTOShouldBeConstructableWithAllFields() {
        bookingDTO.setId("booking-complete-test");
        bookingDTO.setExperienceId("exp-complete-test");
        bookingDTO.setStartDate(new Date());
        bookingDTO.setParticipants(5);
        
        experienceDTO.setId("exp-complete-test");
        experienceDTO.setOrganizer("Complete Test Tours");
        experienceDTO.setEventName("Complete Test Event");
        experienceDTO.setAddress("Test Address");
        experienceDTO.setCity("Test City");
        experienceDTO.setState("TS");
        experienceDTO.setPostcode(12345);
        experienceDTO.setBorough("Test Borough");
        experienceDTO.setLatitude(new BigDecimal("40.0000"));
        experienceDTO.setLongitude(new BigDecimal("-74.0000"));
        experienceDTO.setNta("Test NTA");
        experienceDTO.setCapacity(50);
        experienceDTO.setPricePerPerson(new BigDecimal("99.99"));
        
        experienceBookingDTO.setBooking(bookingDTO);
        experienceBookingDTO.setExperience(experienceDTO);
        experienceBookingDTO.setAmountPaid(new BigDecimal("499.95"));
        experienceBookingDTO.setTripId(123L);
        experienceBookingDTO.setResponsibleUserIds(Set.of(1L, 2L, 3L));
        
        assertNotNull(experienceBookingDTO.getBooking());
        assertNotNull(experienceBookingDTO.getExperience());
        assertEquals("booking-complete-test", experienceBookingDTO.getBooking().getId());
        assertEquals("Complete Test Tours", experienceBookingDTO.getExperience().getOrganizer());
        assertEquals(new BigDecimal("499.95"), experienceBookingDTO.getAmountPaid());
        assertEquals(123L, experienceBookingDTO.getTripId());
        assertEquals(3, experienceBookingDTO.getResponsibleUserIds().size());
        assertTrue(experienceBookingDTO.getResponsibleUserIds().contains(2L));
    }

}
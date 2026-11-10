package codenomads.experience_tracking.controller;

import codenomads.experience_tracking.controller.ExperienceTrackingController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import codenomads.experience_tracking.domain.ExperienceBooking;
import codenomads.experience_tracking.dto.ExperienceBookingDTO;
import codenomads.experience_tracking.service.ExperienceTrackingService;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceTrackingControllerTest {

    @Mock
    private ExperienceTrackingService experienceTrackingService;

    @InjectMocks
    private ExperienceTrackingController experienceTrackingController;

    private ExperienceBookingDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
    }

    @Test
    void healthCheckShouldReturnOkResponse() {
        ResponseEntity<String> response = experienceTrackingController.healthCheck();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Experience tracking service is up and running", response.getBody());
    }

    @Test
    void receiveExperienceBookingValidDTOShouldReturnOkResponse() {
        ExperienceBooking mockBooking = createMockBooking();
        when(experienceTrackingService.saveExperienceBooking(any(ExperienceBookingDTO.class))).thenReturn(mockBooking);

        ResponseEntity<String> response = experienceTrackingController.receiveExperienceBooking(testDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Experience booking received", response.getBody());
        verify(experienceTrackingService, times(1)).saveExperienceBooking(testDTO);
    }

    @Test
    void receiveExperienceBookingServiceThrowsExceptionShouldPropagateException() {
        when(experienceTrackingService.saveExperienceBooking(any(ExperienceBookingDTO.class)))
            .thenThrow(new IllegalArgumentException("Invalid experience data"));

        assertThrows(IllegalArgumentException.class, 
            () -> experienceTrackingController.receiveExperienceBooking(testDTO));
        
        verify(experienceTrackingService, times(1)).saveExperienceBooking(testDTO);
    }

    @Test
    void receiveExperienceBookingRuntimeExceptionShouldPropagateException() {
        when(experienceTrackingService.saveExperienceBooking(any(ExperienceBookingDTO.class)))
            .thenThrow(new RuntimeException("Database connection error"));

        assertThrows(RuntimeException.class, 
            () -> experienceTrackingController.receiveExperienceBooking(testDTO));
        
        verify(experienceTrackingService, times(1)).saveExperienceBooking(testDTO);
    }

    private ExperienceBookingDTO createTestDTO() {
        ExperienceBookingDTO dto = new ExperienceBookingDTO();
        
        ExperienceBookingDTO.BookingDTO booking = new ExperienceBookingDTO.BookingDTO();
        booking.setId("booking-exp-789");
        booking.setExperienceId("exp-101112");
        booking.setStartDate(new Date());
        booking.setParticipants(6);
        
        ExperienceBookingDTO.ExperienceDTO experience = new ExperienceBookingDTO.ExperienceDTO();
        experience.setId("exp-101112");
        experience.setOrganizer("NYC Food Tours");
        experience.setEventName("Brooklyn Food Walking Tour");
        experience.setAddress("Brooklyn Heights Promenade");
        experience.setCity("Brooklyn");
        experience.setState("NY");
        experience.setPostcode(11201);
        experience.setBorough("Brooklyn");
        experience.setLatitude(new BigDecimal("40.6962"));
        experience.setLongitude(new BigDecimal("-73.9961"));
        experience.setNta("DUMBO-Vinegar Hill-Downtown Brooklyn-Boerum Hill");
        experience.setCapacity(15);
        experience.setPricePerPerson(new BigDecimal("75.00"));
        
        dto.setBooking(booking);
        dto.setExperience(experience);
        
        return dto;
    }

    private ExperienceBooking createMockBooking() {
        ExperienceBooking booking = new ExperienceBooking();
        booking.setId(1L);
        booking.setBookingId("booking-exp-789");
        booking.setExperienceId("exp-101112");
        return booking;
    }
}
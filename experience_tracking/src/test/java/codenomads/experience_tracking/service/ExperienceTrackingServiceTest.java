package codenomads.experience_tracking.service;

import codenomads.experience_tracking.service.ExperienceTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.experience_tracking.domain.ExperienceBooking;
import codenomads.experience_tracking.dto.ExperienceBookingDTO;
import codenomads.experience_tracking.gateway.ExpenseGateway;
import codenomads.experience_tracking.gateway.TripGateway;
import codenomads.experience_tracking.repository.ExperienceBookingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceTrackingServiceTest {

    @Mock
    private ExperienceBookingRepository experienceBookingRepository;
    
    @Mock
    private ExpenseGateway expenseGateway;
    
    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private ExperienceTrackingService experienceTrackingService;

    private ExperienceBookingDTO testDTO;
    private ExperienceBooking testBooking;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
        testBooking = createTestBooking();
    }

    @Test
    void saveExperienceBookingValidDTOWithoutPaymentShouldSaveSuccessfully() {
        when(experienceBookingRepository.save(any(ExperienceBooking.class))).thenReturn(testBooking);

        ExperienceBooking result = experienceTrackingService.saveExperienceBooking(testDTO);

        assertNotNull(result);
        assertEquals(testBooking.getBookingId(), result.getBookingId());
        verify(experienceBookingRepository, times(1)).save(any(ExperienceBooking.class));
        verify(expenseGateway, never()).createExpense(any(), any(), any());
        verify(tripGateway, never()).addToTrip(any(), any());
    }

    @Test
    void saveExperienceBookingWithValidPaymentAndTripShouldCreateExpenseAndAddToTrip() {
        testDTO.setAmountPaid(new BigDecimal("250.00"));
        testDTO.setTripId(1L);
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(experienceBookingRepository.save(any(ExperienceBooking.class))).thenReturn(testBooking);
        when(expenseGateway.createExpense(any(), any(), any())).thenReturn(456L);

        ExperienceBooking result = experienceTrackingService.saveExperienceBooking(testDTO);

        assertNotNull(result);
        verify(experienceBookingRepository, times(2)).save(any(ExperienceBooking.class));
        verify(expenseGateway, times(1)).createExpense(testDTO.getAmountPaid(), testDTO.getTripId(), testDTO.getResponsibleUserIds());
        verify(tripGateway, times(1)).addToTrip(testDTO.getTripId(), testBooking.getId());
    }

    @Test
    void saveExperienceBookingWithPaymentButNoTripIdShouldThrowException() {
        testDTO.setAmountPaid(new BigDecimal("250.00"));
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(experienceBookingRepository.save(any(ExperienceBooking.class))).thenReturn(testBooking);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> experienceTrackingService.saveExperienceBooking(testDTO));

        assertEquals("TripId and responsibleUserIds are required when amountPaid is provided", exception.getMessage());
        verify(experienceBookingRepository, times(1)).save(any(ExperienceBooking.class));
    }

    private ExperienceBookingDTO createTestDTO() {
        ExperienceBookingDTO dto = new ExperienceBookingDTO();
        
        ExperienceBookingDTO.BookingDTO booking = new ExperienceBookingDTO.BookingDTO();
        booking.setId("booking-exp-123");
        booking.setExperienceId("exp-456");
        booking.setStartDate(new Date());
        booking.setParticipants(4);
        
        ExperienceBookingDTO.ExperienceDTO experience = new ExperienceBookingDTO.ExperienceDTO();
        experience.setId("exp-456");
        experience.setOrganizer("Adventure Tours NYC");
        experience.setEventName("Central Park Walking Tour");
        experience.setAddress("Central Park South");
        experience.setCity("New York");
        experience.setState("NY");
        experience.setPostcode(10019);
        experience.setBorough("Manhattan");
        experience.setLatitude(new BigDecimal("40.7829"));
        experience.setLongitude(new BigDecimal("-73.9654"));
        experience.setNta("Midtown-Midtown South");
        experience.setCapacity(20);
        experience.setPricePerPerson(new BigDecimal("45.00"));
        
        dto.setBooking(booking);
        dto.setExperience(experience);
        
        return dto;
    }

    private ExperienceBooking createTestBooking() {
        ExperienceBooking booking = new ExperienceBooking();
        booking.setId(1L);
        booking.setBookingId("booking-exp-123");
        booking.setExperienceId("exp-456");
        booking.setStartDate(new Date());
        booking.setParticipants(4);
        booking.setOrganizer("Adventure Tours NYC");
        booking.setEventName("Central Park Walking Tour");
        booking.setAddress("Central Park South");
        booking.setCity("New York");
        booking.setState("NY");
        booking.setPostcode(10019);
        booking.setBorough("Manhattan");
        booking.setLatitude(new BigDecimal("40.7829"));
        booking.setLongitude(new BigDecimal("-73.9654"));
        booking.setNta("Midtown-Midtown South");
        booking.setCapacity(20);
        booking.setPricePerPerson(new BigDecimal("45.00"));
        return booking;
    }
}
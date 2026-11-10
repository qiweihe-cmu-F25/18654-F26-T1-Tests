package codenomads.flight_tracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.flight_tracking.domain.FlightBooking;
import codenomads.flight_tracking.dto.FlightBookingDTO;
import codenomads.flight_tracking.gateway.ExpenseGateway;
import codenomads.flight_tracking.gateway.TripGateway;
import codenomads.flight_tracking.repository.FlightBookingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightTrackingServiceTest {

    @Mock
    private FlightBookingRepository flightBookingRepository;
    
    @Mock
    private ExpenseGateway expenseGateway;
    
    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private FlightTrackingService flightTrackingService;

    private FlightBookingDTO testDTO;
    private FlightBooking testBooking;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
        testBooking = createTestBooking();
    }

    @Test
    void saveFlightBookingValidDTOWithoutPaymentShouldSaveSuccessfully() {
        when(flightBookingRepository.save(any(FlightBooking.class))).thenReturn(testBooking);

        FlightBooking result = flightTrackingService.saveFlightBooking(testDTO);

        assertNotNull(result);
        assertEquals(testBooking.getBookingId(), result.getBookingId());
        verify(flightBookingRepository, times(1)).save(any(FlightBooking.class));
        verify(expenseGateway, never()).createExpense(any(), any(), any());
        verify(tripGateway, never()).addToTrip(any(), any());
    }

    @Test
    void saveFlightBookingWithValidPaymentAndTripShouldCreateExpenseAndAddToTrip() {
        testDTO.setAmountPaid(new BigDecimal("450.00"));
        testDTO.setTripId(1L);
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(flightBookingRepository.save(any(FlightBooking.class))).thenReturn(testBooking);
        when(expenseGateway.createExpense(any(), any(), any())).thenReturn(789L);

        FlightBooking result = flightTrackingService.saveFlightBooking(testDTO);

        assertNotNull(result);
        verify(flightBookingRepository, times(2)).save(any(FlightBooking.class));
        verify(expenseGateway, times(1)).createExpense(testDTO.getAmountPaid(), testDTO.getTripId(), testDTO.getResponsibleUserIds());
        verify(tripGateway, times(1)).addToTrip(testDTO.getTripId(), testBooking.getId());
    }

    @Test
    void saveFlightBookingWithPaymentButNoTripIdShouldThrowException() {
        testDTO.setAmountPaid(new BigDecimal("450.00"));
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(flightBookingRepository.save(any(FlightBooking.class))).thenReturn(testBooking);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> flightTrackingService.saveFlightBooking(testDTO));

        assertEquals("TripId and responsibleUserIds are required when amountPaid is provided", exception.getMessage());
        verify(flightBookingRepository, times(1)).save(any(FlightBooking.class));
    }

    private FlightBookingDTO createTestDTO() {
        FlightBookingDTO dto = new FlightBookingDTO();
        
        FlightBookingDTO.FlightData data = new FlightBookingDTO.FlightData();
        data.setBookingId(UUID.randomUUID());
        data.setFlightId(UUID.randomUUID());
        data.setCarrier("American Airlines");
        data.setFlightNumber("AA1234");
        data.setOrigin("JFK");
        data.setDestination("LAX");
        data.setDistanceMiles(2475);
        data.setTotalSeats(180);
        data.setAvailableSeats(45);
        data.setAircraft("Boeing 737");
        data.setAirtime(360);
        data.setDepartureDateTime(new Date());
        data.setFlightPrice(new BigDecimal("299.99"));
        
        dto.setData(data);
        
        return dto;
    }

    private FlightBooking createTestBooking() {
        FlightBooking booking = new FlightBooking();
        booking.setId(1L);
        booking.setBookingId(UUID.randomUUID());
        booking.setFlightId(UUID.randomUUID());
        booking.setCarrier("American Airlines");
        booking.setFlightNumber("AA1234");
        booking.setOrigin("JFK");
        booking.setDestination("LAX");
        booking.setDistanceMiles(2475);
        booking.setTotalSeats(180);
        booking.setAvailableSeats(45);
        booking.setAircraft("Boeing 737");
        booking.setAirtime(360);
        booking.setDepartureDateTime(new Date());
        booking.setFlightPrice(new BigDecimal("299.99"));
        return booking;
    }
}
package codenomads.carhire_tracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.carhire_tracking.domain.CarHireBooking;
import codenomads.carhire_tracking.dto.CarHireBookingDTO;
import codenomads.carhire_tracking.gateway.ExpenseGateway;
import codenomads.carhire_tracking.gateway.TripGateway;
import codenomads.carhire_tracking.repository.CarHireBookingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarHireTrackingServiceTest {

    @Mock
    private CarHireBookingRepository carHireBookingRepository;
    
    @Mock
    private ExpenseGateway expenseGateway;
    
    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private CarHireTrackingService carHireTrackingService;

    private CarHireBookingDTO testDTO;
    private CarHireBooking testBooking;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
        testBooking = createTestBooking();
    }

    @Test
    void saveCarHireBookingValidDTOWithoutPaymentShouldSaveSuccessfully() {
        when(carHireBookingRepository.save(any(CarHireBooking.class))).thenReturn(testBooking);

        CarHireBooking result = carHireTrackingService.saveCarHireBooking(testDTO);

        assertNotNull(result);
        assertEquals(testBooking.getBookingId(), result.getBookingId());
        verify(carHireBookingRepository, times(1)).save(any(CarHireBooking.class));
        verify(expenseGateway, never()).createExpense(any(), any(), any());
        verify(tripGateway, never()).addToTrip(any(), any());
    }

    @Test
    void saveCarHireBookingWithValidPaymentAndTripShouldCreateExpenseAndAddToTrip() {
        testDTO.setAmountPaid(new BigDecimal("150.00"));
        testDTO.setTripId(1L);
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(carHireBookingRepository.save(any(CarHireBooking.class))).thenReturn(testBooking);
        when(expenseGateway.createExpense(any(), any(), any())).thenReturn(123L);

        CarHireBooking result = carHireTrackingService.saveCarHireBooking(testDTO);

        assertNotNull(result);
        verify(carHireBookingRepository, times(2)).save(any(CarHireBooking.class));
        verify(expenseGateway, times(1)).createExpense(testDTO.getAmountPaid(), testDTO.getTripId(), testDTO.getResponsibleUserIds());
        verify(tripGateway, times(1)).addToTrip(testDTO.getTripId(), testBooking.getId());
    }

    @Test
    void saveCarHireBooking_WithPaymentButNoTripIdShouldThrowException() {
        testDTO.setAmountPaid(new BigDecimal("150.00"));
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(carHireBookingRepository.save(any(CarHireBooking.class))).thenReturn(testBooking);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> carHireTrackingService.saveCarHireBooking(testDTO));

        assertEquals("TripId and responsibleUserIds are required when amountPaid is provided", exception.getMessage());
        verify(carHireBookingRepository, times(1)).save(any(CarHireBooking.class));
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

    private CarHireBooking createTestBooking() {
        CarHireBooking booking = new CarHireBooking();
        booking.setId(1L);
        booking.setBookingId("booking-123");
        booking.setCarHireId("car-456");
        booking.setOperator("Test Operator");
        booking.setLicensePlate("ABC123");
        booking.setVin("1HGCM82633A123456");
        booking.setVehicleYear(2023);
        booking.setPhone("555-1234");
        booking.setWebsite("www.test.com");
        booking.setDailyRate(new BigDecimal("50.00"));
        booking.setVehicleType("SUV");
        booking.setSeatCapacity(5);
        booking.setStartDate(new Date());
        booking.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        return booking;
    }
}
package codenomads.hotel_tracking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.hotel_tracking.domain.HotelBooking;
import codenomads.hotel_tracking.dto.HotelBookingDTO;
import codenomads.hotel_tracking.gateway.ExpenseGateway;
import codenomads.hotel_tracking.gateway.TripGateway;
import codenomads.hotel_tracking.repository.HotelBookingRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelTrackingServiceTest {

    @Mock
    private HotelBookingRepository hotelBookingRepository;
    
    @Mock
    private ExpenseGateway expenseGateway;
    
    @Mock
    private TripGateway tripGateway;

    @InjectMocks
    private HotelTrackingService hotelTrackingService;

    private HotelBookingDTO testDTO;
    private HotelBooking testBooking;

    @BeforeEach
    void setUp() {
        testDTO = createTestDTO();
        testBooking = createTestBooking();
    }

    @Test
    void saveHotelBookingValidDTOWithoutPaymentShouldSaveSuccessfully() {
        when(hotelBookingRepository.save(any(HotelBooking.class))).thenReturn(testBooking);

        HotelBooking result = hotelTrackingService.saveHotelBooking(testDTO);

        assertNotNull(result);
        assertEquals(testBooking.getBookingId(), result.getBookingId());
        verify(hotelBookingRepository, times(1)).save(any(HotelBooking.class));
        verify(expenseGateway, never()).createExpense(any(), any(), any());
        verify(tripGateway, never()).addToTrip(any(), any());
    }

    @Test
    void saveHotelBookingWithPaymentButNoTripIdShouldThrowException() {
        testDTO.setAmountPaid(new BigDecimal("320.00"));
        testDTO.setResponsibleUserIds(Set.of(1L, 2L));
        
        when(hotelBookingRepository.save(any(HotelBooking.class))).thenReturn(testBooking);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> hotelTrackingService.saveHotelBooking(testDTO));

        assertEquals("TripId and responsibleUserIds are required when amountPaid is provided", exception.getMessage());
        verify(hotelBookingRepository, times(1)).save(any(HotelBooking.class));
    }

    @Test
    void saveHotelBookingBookingDetailsShouldMapCorrectly() {
        when(hotelBookingRepository.save(any(HotelBooking.class))).thenReturn(testBooking);

        HotelBooking result = hotelTrackingService.saveHotelBooking(testDTO);

        assertNotNull(result);
        verify(hotelBookingRepository, times(1)).save(argThat(booking ->
            booking.getStartDate().equals(testDTO.getData().getBooking().getStartDate()) &&
            booking.getEndDate().equals(testDTO.getData().getBooking().getEndDate()) &&
            booking.getOnePersonRooms().equals(testDTO.getData().getBooking().getOnePersonRooms()) &&
            booking.getTwoPersonRooms().equals(testDTO.getData().getBooking().getTwoPersonRooms()) &&
            booking.getFourPersonRooms().equals(testDTO.getData().getBooking().getFourPersonRooms())
        ));
    }

    private HotelBookingDTO createTestDTO() {
        HotelBookingDTO dto = new HotelBookingDTO();
        
        HotelBookingDTO.HotelData data = new HotelBookingDTO.HotelData();
        
        HotelBookingDTO.HotelDTO hotel = new HotelBookingDTO.HotelDTO();
        hotel.setHotelId("hotel-123");
        hotel.setBorocode(1);
        hotel.setBlock(456);
        hotel.setLot(789);
        hotel.setStreetNumber("123");
        hotel.setStreetName("Broadway");
        hotel.setZipcode(10019);
        hotel.setBuildingClass("R4");
        hotel.setOwnerName("Marriott International");
        hotel.setBorough("Manhattan");
        hotel.setLatitude("40.7589");
        hotel.setLongitude("-73.9851");
        hotel.setArea("Midtown");
        
        HotelBookingDTO.BookingDTO booking = new HotelBookingDTO.BookingDTO();
        booking.setId("booking-hotel-456");
        booking.setHotelId("hotel-123");
        booking.setStartDate(new Date());
        booking.setEndDate(new Date(System.currentTimeMillis() + 86400000 * 3)); // 3 days later
        booking.setOnePersonRooms(1);
        booking.setTwoPersonRooms(2);
        booking.setFourPersonRooms(0);
        
        data.setHotel(hotel);
        data.setBooking(booking);
        dto.setData(data);
        
        return dto;
    }

    private HotelBooking createTestBooking() {
        HotelBooking booking = new HotelBooking();
        booking.setId(1L);
        booking.setBookingId("booking-hotel-456");
        booking.setHotelId("hotel-123");
        booking.setBorocode(1);
        booking.setBlock(456);
        booking.setLot(789);
        booking.setStreetNumber("123");
        booking.setStreetName("Broadway");
        booking.setZipcode(10019);
        booking.setBuildingClass("R4");
        booking.setOwnerName("Marriott International");
        booking.setBorough("Manhattan");
        booking.setLatitude("40.7589");
        booking.setLongitude("-73.9851");
        booking.setArea("Midtown");
        booking.setStartDate(new Date());
        booking.setEndDate(new Date(System.currentTimeMillis() + 86400000 * 3));
        booking.setOnePersonRooms(1);
        booking.setTwoPersonRooms(2);
        booking.setFourPersonRooms(0);
        return booking;
    }
}
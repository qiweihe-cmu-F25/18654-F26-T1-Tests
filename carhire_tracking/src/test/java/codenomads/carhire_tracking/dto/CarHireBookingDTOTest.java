package codenomads.carhire_tracking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CarHireBookingDTOTest {

    private CarHireBookingDTO carHireBookingDTO;
    private CarHireBookingDTO.CarHireData carHireData;
    private CarHireBookingDTO.CarHireDTO carHireDTO;
    private CarHireBookingDTO.BookingDTO bookingDTO;

    @BeforeEach
    void setUp() {
        carHireBookingDTO = new CarHireBookingDTO();
        carHireData = new CarHireBookingDTO.CarHireData();
        carHireDTO = new CarHireBookingDTO.CarHireDTO();
        bookingDTO = new CarHireBookingDTO.BookingDTO();
    }

    @Test
    void setAndGetAmountPaidShouldWorkCorrectly() {
        BigDecimal expectedAmount = new BigDecimal("150.75");
        carHireBookingDTO.setAmountPaid(expectedAmount);
        
        assertEquals(expectedAmount, carHireBookingDTO.getAmountPaid());
    }

    @Test
    void setAndGetTripIdShouldWorkCorrectly() {
        Long expectedTripId = 456L;
        carHireBookingDTO.setTripId(expectedTripId);
        
        assertEquals(expectedTripId, carHireBookingDTO.getTripId());
    }

    @Test
    void setAndGetResponsibleUserIdsShouldWorkCorrectly() {
        Set<Long> expectedUserIds = Set.of(1L, 2L, 3L);
        carHireBookingDTO.setResponsibleUserIds(expectedUserIds);
        
        assertEquals(expectedUserIds, carHireBookingDTO.getResponsibleUserIds());
    }

    @Test
    void setAndGetDataShouldWorkCorrectly() {
        carHireBookingDTO.setData(carHireData);
        
        assertEquals(carHireData, carHireBookingDTO.getData());
    }

    @Test
    void carHireData_setAndGetCarhireShouldWorkCorrectly() {
        carHireData.setCarhire(carHireDTO);
        
        assertEquals(carHireDTO, carHireData.getCarhire());
    }

    @Test
    void carHireData_setAndGetBookingShouldWorkCorrectly() {
        carHireData.setBooking(bookingDTO);
        
        assertEquals(bookingDTO, carHireData.getBooking());
    }
}
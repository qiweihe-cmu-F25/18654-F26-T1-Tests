package codenomads.tripmanagement.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    private Trip trip;
    private Date testStartDate;
    private Date testEndDate;

    @BeforeEach
    void setUp() {
        trip = new Trip();
        testStartDate = new Date();
        testEndDate = new Date(System.currentTimeMillis() + 86400000);
    }

    @Test
    void setAndGetStartDateShouldWorkCorrectly() {
        trip.setStartDate(testStartDate);
        
        assertEquals(testStartDate, trip.getStartDate());
    }

    @Test
    void setAndGetEndDateShouldWorkCorrectly() {
        trip.setEndDate(testEndDate);
        
        assertEquals(testEndDate, trip.getEndDate());
    }

    @Test
    void setAndGetCreatorIdShouldWorkCorrectly() {
        Long expectedCreatorId = 456L;
        trip.setCreatorId(expectedCreatorId);
        
        assertEquals(expectedCreatorId, trip.getCreatorId());
    }


    @Test
    void removeMemberExistingMemberShouldRemoveFromSet() {
        TripMember member1 = new TripMember();
        member1.setUserId(1L);
        member1.setRole(TripMember.Role.MEMBER);

        TripMember member2 = new TripMember();
        member2.setUserId(2L);
        member2.setRole(TripMember.Role.MEMBER);

        trip.addMember(member1);
        trip.addMember(member2);
        
        assertEquals(2, trip.getMembers().size());

        trip.removeMember(1L);

        assertEquals(1, trip.getMembers().size());
        assertFalse(trip.getMembers().stream().anyMatch(m -> m.getUserId().equals(1L)));
        assertTrue(trip.getMembers().stream().anyMatch(m -> m.getUserId().equals(2L)));
    }

    @Test
    void bookingInnerClassSetAndGetFieldsShouldWorkCorrectly() {
        Trip.Booking booking = new Trip.Booking();
        Long expectedBookingId = 789L;
        BookingSource expectedSource = BookingSource.FLIGHT;

        booking.setBookingId(expectedBookingId);
        booking.setSource(expectedSource);

        assertEquals(expectedBookingId, booking.getBookingId());
        assertEquals(expectedSource, booking.getSource());
    }
}
package codenomads.tripmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import codenomads.tripmanagement.domain.BookingSource;
import codenomads.tripmanagement.domain.Trip;
import codenomads.tripmanagement.domain.TripMember;
import codenomads.tripmanagement.dto.CreateTripRequest;
import codenomads.tripmanagement.dto.UpdateTripRequest;
import codenomads.tripmanagement.exception.CustomException;
import codenomads.tripmanagement.gateway.UserProfileGateway;
import codenomads.tripmanagement.repository.TripRepository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripManagementServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserProfileGateway userProfileGateway;

    @Mock
    private RequestAttributes requestAttributes;

    @InjectMocks
    private TripManagementService tripManagementService;

    private CreateTripRequest testRequest;
    private Trip testTrip;

    @BeforeEach
    void setUp() {
        testRequest = createTestRequest();
        testTrip = createTestTrip();
        RequestContextHolder.setRequestAttributes(requestAttributes);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void createTripValidRequestWithMembersShouldCreateTripWithAllMembers() {
        testRequest.setMembers(Arrays.asList(1L, 2L, 3L));
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Trip result = tripManagementService.createTrip(testRequest, 1L);

        assertNotNull(result);
        verify(tripRepository, times(1)).save(argThat(trip -> 
            trip.getTripName().equals(testRequest.getTripName()) &&
            trip.getStartDate().equals(testRequest.getStartDate()) &&
            trip.getEndDate().equals(testRequest.getEndDate()) &&
            trip.getCreatorId().equals(1L) &&
            trip.getMembers().size() == 3 // Creator + 2 additional members
        ));
    }

    @Test
    void createTripValidRequestWithoutMembersShouldCreateTripWithCreatorOnly() {
        testRequest.setMembers(null);
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Trip result = tripManagementService.createTrip(testRequest, 1L);

        assertNotNull(result);
        verify(tripRepository, times(1)).save(argThat(trip -> 
            trip.getCreatorId().equals(1L) &&
            trip.getMembers().size() == 1 // Only creator
        ));
    }

    @Test
    void createTripCreatorInMembersListShouldNotDuplicateCreator() {
        testRequest.setMembers(Arrays.asList(1L, 2L)); // Creator (1L) is in members list
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Trip result = tripManagementService.createTrip(testRequest, 1L);

        assertNotNull(result);
        verify(tripRepository, times(1)).save(argThat(trip -> 
            trip.getMembers().size() == 2 // Creator + 1 additional member (no duplication)
        ));
    }

    @Test
    void createTripWhenStartDateAfterEndDateShouldThrowIllegalArgumentException() {
        testRequest.setStartDate(new Date(System.currentTimeMillis() + 86400000));
        testRequest.setEndDate(new Date());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> tripManagementService.createTrip(testRequest, 1L));

        assertEquals("startDate must not be after endDate", exception.getMessage());
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void addBookingToTripAsMemberShouldSucceedWithoutCreatorCheck() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(testTrip));
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Optional<Trip> result = tripManagementService.addBookingToTrip(1L, 99L, BookingSource.HOTEL);

        assertTrue(result.isPresent());
        verify(tripRepository, times(1)).save(any(Trip.class));
        // No RequestContextHolder userid needed — creator check was removed for bookings
    }

    @Test
    void updateTripAsNonCreatorShouldReturnForbidden() {
        when(requestAttributes.getAttribute("userid", RequestAttributes.SCOPE_REQUEST)).thenReturn(2L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(testTrip));

        UpdateTripRequest updateRequest = new UpdateTripRequest();
        updateRequest.setTripName("Updated");
        updateRequest.setStartDate(testTrip.getStartDate());
        updateRequest.setEndDate(testTrip.getEndDate());
        updateRequest.setMembers(Arrays.asList(1L, 2L));

        CustomException exception = assertThrows(CustomException.class,
            () -> tripManagementService.updateTrip(1L, updateRequest));

        assertEquals(403, exception.getCode());
        assertEquals("Only creator can modify trip", exception.getMessage());
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void addMemberToTripWhenUserDoesNotExistShouldThrowNotFound() {
        when(requestAttributes.getAttribute("userid", RequestAttributes.SCOPE_REQUEST)).thenReturn(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(testTrip));
        doThrow(new CustomException("User not found", 404))
            .when(userProfileGateway).ensureUserExists(999L);

        CustomException exception = assertThrows(CustomException.class,
            () -> tripManagementService.addMemberToTrip(1L, 999L, TripMember.Role.MEMBER));

        assertEquals(404, exception.getCode());
        assertEquals("User not found", exception.getMessage());
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void addMemberToTripWhenUserExistsShouldAddMember() {
        when(requestAttributes.getAttribute("userid", RequestAttributes.SCOPE_REQUEST)).thenReturn(1L);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(testTrip));
        doNothing().when(userProfileGateway).ensureUserExists(5L);
        when(tripRepository.save(any(Trip.class))).thenReturn(testTrip);

        Optional<Trip> result = tripManagementService.addMemberToTrip(1L, 5L, TripMember.Role.MEMBER);

        assertTrue(result.isPresent());
        verify(userProfileGateway, times(1)).ensureUserExists(5L);
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    void isUserInTripUserInTripShouldReturnTrue() {
        when(tripRepository.existsByIdAndMembersUserId(1L, 1L)).thenReturn(true);

        boolean result = tripManagementService.isUserInTrip(1L, 1L);

        assertTrue(result);
        verify(tripRepository, times(1)).existsByIdAndMembersUserId(1L, 1L);
    }

    private CreateTripRequest createTestRequest() {
        CreateTripRequest request = new CreateTripRequest();
        request.setTripName("Test Trip");
        request.setStartDate(new Date());
        request.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        return request;
    }

    private Trip createTestTrip() {
        Trip trip = new Trip();
        trip.setId(1L);
        trip.setTripName("Test Trip");
        trip.setStartDate(new Date());
        trip.setEndDate(new Date(System.currentTimeMillis() + 86400000));
        trip.setCreatorId(1L);
        return trip;
    }
}
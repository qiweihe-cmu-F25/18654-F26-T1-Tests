package codenomads.tripmanagement.service;

import codenomads.tripmanagement.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codenomads.tripmanagement.domain.Trip;
import codenomads.tripmanagement.domain.TripMember;
import codenomads.tripmanagement.dto.CreateTripRequest;
import codenomads.tripmanagement.dto.UpdateTripRequest;
import codenomads.tripmanagement.domain.BookingSource;
import codenomads.tripmanagement.gateway.UserProfileGateway;
import codenomads.tripmanagement.repository.TripRepository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.*;

@Service
public class TripManagementService {

    private final TripRepository tripRepository;
    private final UserProfileGateway userProfileGateway;

    @Autowired
    public TripManagementService(TripRepository tripRepository, UserProfileGateway userProfileGateway) {
        this.tripRepository = tripRepository;
        this.userProfileGateway = userProfileGateway;
    }

    @Transactional
    public Trip createTrip(CreateTripRequest request, Long creatorId) {
        // --- date guard (reject inconsistent dates) ---
        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getStartDate().after(request.getEndDate())) {
            throw new IllegalArgumentException("startDate must not be after endDate");
        }

        if (request.getTripName() == null || request.getTripName().trim().isEmpty()) {
            throw new IllegalArgumentException("tripName must be non-blank");
        }
        
        Trip trip = new Trip();
        trip.setTripName(request.getTripName());
        trip.setStartDate(request.getStartDate());
        trip.setEndDate(request.getEndDate()); 
        trip.setCreatorId(creatorId);

         // --- normalize members to set semantics; ensure creator exactly once ---
        Set<Long> normalized = new LinkedHashSet<>();
        normalized.add(creatorId); // creator always present

        if (request.getMembers() != null) {
            for (Long id : request.getMembers()) {
                if (id != null) {
                    normalized.add(id);
                }
            }
        }

        for (Long id : normalized) {
            TripMember m = new TripMember();
            m.setUserId(id);
            m.setRole(id.equals(creatorId) ? TripMember.Role.CREATOR : TripMember.Role.MEMBER);
            trip.addMember(m);
        }
        
        return tripRepository.save(trip);
    }

    @Transactional
    public Optional<Trip> addBookingToTrip(Long tripId, Long bookingId, BookingSource source) {
        return tripRepository.findById(tripId).map(trip -> {
            if (bookingId == null || source == null) {
                throw new IllegalArgumentException("bookingId and source are required");
            }

            // Membership is checked in the controller; any trip member can add a booking.

            // Idempotency: dedupe on (bookingId, source)
            boolean exists = trip.getBookings().stream().anyMatch(b ->
                Objects.equals(bookingId, b.getBookingId()) &&
                b.getSource() == source // enum compare: use ==
            );
    
            if (!exists) {
                Trip.Booking booking = new Trip.Booking();
                booking.setBookingId(bookingId);
                booking.setSource(source);
                trip.getBookings().add(booking);
                tripRepository.save(trip);
            }
    
            return trip;
        });
    }
    //if the member already exists, no duplicate is added.
    @Transactional
    public Optional<Trip> addMemberToTrip(Long tripId, Long userId, TripMember.Role role) {
        // Only the creator of a trip can make changes to a trip
        checkUserCreatorOfTrip(tripId);
        userProfileGateway.ensureUserExists(userId);

        // A trip has exactly one creator (Trip.creatorId), assigned at creation
        if (role == TripMember.Role.CREATOR) {
            throw new IllegalArgumentException("A trip can only have one creator");
        }

        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        optionalTrip.ifPresent(trip -> {
            boolean exists = trip.getMembers().stream()
                .anyMatch(m -> m.getUserId().equals(userId));
            if (!exists) {
                TripMember member = new TripMember();
                member.setUserId(userId);
                member.setRole(role);
                trip.addMember(member);
                tripRepository.save(trip);
            }
        });
        return optionalTrip;
    }

    @Transactional
    public Optional<Trip> removeMemberFromTrip(Long tripId, Long userId) {
        // Only the creator of a trip can make changes to a trip
        checkUserCreatorOfTrip(tripId);

        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        optionalTrip.ifPresent(trip -> {
            if (trip.getCreatorId() != null && trip.getCreatorId().equals(userId)) {
                throw new IllegalArgumentException("Cannot remove the creator from the trip");
            }

            if (!isUserInTrip(tripId, userId)) {
                throw new IllegalArgumentException("User " + userId + " is not a member of trip " + tripId);
            }

            trip.removeMember(userId);
            tripRepository.save(trip);
        });
        return optionalTrip;
    }

    @Transactional
    public Trip updateTrip(Long tripId, UpdateTripRequest updatedTrip) {
        // Only the creator of a trip can make changes to a trip
        checkUserCreatorOfTrip(tripId);

        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        if (optionalTrip.isPresent()) {
            // --- date guard (reject inconsistent dates) ---
            if (updatedTrip.getStartDate() != null && updatedTrip.getEndDate() != null
                    && updatedTrip.getStartDate().after(updatedTrip.getEndDate())) {
                throw new IllegalArgumentException("startDate must not be after endDate");
            }
            if (updatedTrip.getTripName() == null || updatedTrip.getTripName().trim().isEmpty()) {
                throw new IllegalArgumentException("tripName must be non-blank");
            }

            Trip trip = optionalTrip.get();
            trip.setTripName(updatedTrip.getTripName());
            trip.setStartDate(updatedTrip.getStartDate());
            trip.setEndDate(updatedTrip.getEndDate());
            // members in the request body are accepted for compatibility but not applied here;
            // use /trips/{tripId}/members endpoints to manage membership.
            return tripRepository.save(trip);
        }
        return null;
    }

    @Transactional
    public void removeTrip(Long tripId) {
        // Only the creator of a trip can make changes to a trip
        checkUserCreatorOfTrip(tripId);

        tripRepository.deleteById(tripId);
    }

    public List<Trip> queryTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> queryTripById(Long tripId) {
        return tripRepository.findById(tripId);
    }

    public boolean isUserInTrip(Long tripId, Long userId) {
        return tripRepository.existsByIdAndMembersUserId(tripId, userId);
    }

    // Any member of a trip can add bookings to it
    public void checkUserMemberOfTrip(Long tripId) {
        Optional<Long> userId = Optional.ofNullable((Long) RequestContextHolder.getRequestAttributes()
                .getAttribute("userid", RequestAttributes.SCOPE_REQUEST));
        if (this.queryTripById(tripId).isEmpty()) {
            throw new CustomException("Trip not found", 404);
        }
        if (userId.isEmpty() || !isUserInTrip(tripId, userId.get())) {
            throw new CustomException("User is not in trip", 401);
        }
    }

    // Only the creator of trip can edit a trip
    public void checkUserCreatorOfTrip(Long tripId) {
        // Retrieve userid from request attribute
        Optional<Long> userId = Optional.ofNullable((Long) RequestContextHolder.getRequestAttributes()
                .getAttribute("userid", RequestAttributes.SCOPE_REQUEST));
        Optional<Trip> currentTrip = this.queryTripById(tripId);
        if (currentTrip.isEmpty()) {
            throw new CustomException("Trip not found", 404);
        }
        if (userId.isEmpty() || !userId.get().equals(currentTrip.get().getCreatorId())) {
            throw new CustomException("Only creator can modify trip", 403);
        }
    }
}

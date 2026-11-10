package codenomads.tripmanagement.controller;

import codenomads.tripmanagement.domain.BookingSource;
import codenomads.tripmanagement.domain.Trip;
import codenomads.tripmanagement.dto.AddTripMemberRequest;
import codenomads.tripmanagement.dto.CreateTripRequest;
import codenomads.tripmanagement.dto.UpdateTripRequest;
import codenomads.tripmanagement.exception.CustomException;
import codenomads.tripmanagement.service.TripManagementService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/trips")
public class TripManagementController {

    private final TripManagementService tripService;
    private static final Logger logger = Logger.getLogger(TripManagementController.class.getName());

    @Autowired
    public TripManagementController(TripManagementService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<Trip> createTrip(@Valid @RequestBody CreateTripRequest request) {
        Long userId = getRequesterId().get();
        logger.info("Received request to create trip: " + request);
        // Check if user is in list of members
        if (request != null && request.getMembers() != null && !request.getMembers().contains(userId)) {
            throw new CustomException("User is not among members of trip", 401);
        }
        Trip savedTrip = tripService.createTrip(request, userId);
        return ResponseEntity.ok(savedTrip);
    }

    @PostMapping("/{tripId}/bookings")
    public ResponseEntity<Trip> addBookingToTrip(
            @PathVariable Long tripId,
            @Valid @RequestBody TripBookingRequest request) {
        logger.info("Received request to add booking to trip: " + request);
        Optional<Long> requesterId = getRequesterId();
        if (!tripService.isUserInTrip(tripId, requesterId.get())) {
            throw new CustomException("User is not in trip", 401);
        }
        Optional<Trip> optionalTrip = tripService.addBookingToTrip(tripId, request.getBookingId(), request.getSource());
        return optionalTrip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable Long tripId,
            @Valid @RequestBody UpdateTripRequest updatedTrip) {
        logger.info("Received request to update trip: " + updatedTrip);
        Trip trip = tripService.updateTrip(tripId, updatedTrip);
        return trip != null ? ResponseEntity.ok(trip) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripId) {
        logger.info("Received request to delete trip with id: " + tripId);
        tripService.removeTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        logger.info("Received request to get all trips");
        List<Trip> trips = tripService.queryTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripById(@PathVariable Long tripId) {
        logger.info("Received request to get trip with id: " + tripId);
        Optional<Trip> optionalTrip = tripService.queryTripById(tripId);
        return optionalTrip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lets other services ask whether the caller may add a booking to a trip before doing any work
    @GetMapping("/{tripId}/can-add-booking")
    public ResponseEntity<Void> canAddBookingToTrip(@PathVariable Long tripId) {
        logger.info("Received request to check if requester can add a booking to trip: " + tripId);
        tripService.checkUserMemberOfTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        logger.info("Received health check request");
        return ResponseEntity.ok("Trip Management service is running.");
    }

    @PostMapping("/{tripId}/members")
    public ResponseEntity<Trip> addMemberToTrip(
            @PathVariable Long tripId,
            @Valid @RequestBody AddTripMemberRequest request) {
        logger.info("Received request to add member to trip: " + request);
        Optional<Trip> optionalTrip = tripService.addMemberToTrip(tripId, request.getUserId(), request.getRole());
        return optionalTrip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{tripId}/members/{userId}")
    public ResponseEntity<Trip> removeMemberFromTrip(
            @PathVariable Long tripId,
            @PathVariable Long userId) {
        logger.info("Received request to remove member from trip: " + userId);
        // TODO: double check by Simeng
        Optional<Trip> optionalTrip = tripService.removeMemberFromTrip(tripId, userId);
        return optionalTrip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{tripId}/members/{userId}")
    public ResponseEntity<Boolean> isUserInTrip(
            @PathVariable Long tripId,
            @PathVariable Long userId) {
        logger.info("Received request to check if user is in trip: " + userId);

        Optional<Long> requesterId = getRequesterId();

        if (!tripService.isUserInTrip(tripId, requesterId.get())) {
            throw new CustomException("Not Authorized", 401);
        }

        boolean isInTrip = tripService.isUserInTrip(tripId, userId);
        return ResponseEntity.ok(isInTrip);
    }

    private Optional<Long> getRequesterId() {
        Optional<Long> requesterId = Optional.ofNullable((Long) RequestContextHolder.getRequestAttributes()
            .getAttribute("userid", RequestAttributes.SCOPE_REQUEST));

        if (!requesterId.isPresent()) {
            throw new CustomException("requesterId is not present", 400);
        }
        return requesterId;
    }

    @Data
    public static class TripBookingRequest {
        private Long bookingId;
        private BookingSource source;
    }
}
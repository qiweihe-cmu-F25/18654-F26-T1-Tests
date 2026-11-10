package codenomads.flight_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codenomads.flight_tracking.domain.FlightBooking;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, String> {
}
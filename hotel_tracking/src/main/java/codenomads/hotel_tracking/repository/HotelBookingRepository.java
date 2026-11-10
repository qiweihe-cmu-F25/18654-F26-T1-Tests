package codenomads.hotel_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codenomads.hotel_tracking.domain.HotelBooking;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, String> {
}
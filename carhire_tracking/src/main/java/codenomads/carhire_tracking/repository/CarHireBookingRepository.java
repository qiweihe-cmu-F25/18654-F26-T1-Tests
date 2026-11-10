package codenomads.carhire_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import codenomads.carhire_tracking.domain.CarHireBooking;

public interface CarHireBookingRepository extends JpaRepository<CarHireBooking, String> {
}
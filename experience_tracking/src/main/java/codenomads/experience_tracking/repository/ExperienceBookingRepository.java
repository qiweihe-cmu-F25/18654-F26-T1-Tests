package codenomads.experience_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codenomads.experience_tracking.domain.ExperienceBooking;

public interface ExperienceBookingRepository extends JpaRepository<ExperienceBooking, Long> {
}
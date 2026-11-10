package codenomads.tripmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codenomads.tripmanagement.domain.Trip;

public interface TripRepository extends JpaRepository<Trip, Long> {
    boolean existsByIdAndMembersUserId(Long tripId, Long userId);
}

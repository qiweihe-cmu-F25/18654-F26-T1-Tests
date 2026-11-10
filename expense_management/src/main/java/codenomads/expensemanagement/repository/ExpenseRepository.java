package codenomads.expensemanagement.repository;

import codenomads.expensemanagement.domain.Expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByTripId(Long tripId);
    
    @Query("SELECT e FROM Expense e WHERE e.tripId = :tripId AND e.occurredOn >= :fromInclusive AND e.occurredOn < :toExclusive")
    List<Expense> findByTripIdAndOccurredOnBetween(
        @Param("tripId") Long tripId, 
        @Param("fromInclusive") LocalDate fromInclusive, 
        @Param("toExclusive") LocalDate toExclusive
    );
}

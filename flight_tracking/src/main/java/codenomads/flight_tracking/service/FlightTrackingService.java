package codenomads.flight_tracking.service;

import java.util.UUID;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codenomads.flight_tracking.domain.FlightBooking;
import codenomads.flight_tracking.dto.FlightBookingDTO;
import codenomads.flight_tracking.gateway.ExpenseGateway;
import codenomads.flight_tracking.gateway.TripGateway;
import codenomads.flight_tracking.repository.FlightBookingRepository;
import jakarta.transaction.Transactional;

@Service
public class FlightTrackingService {

    private final FlightBookingRepository flightBookingRepository;
    private final ExpenseGateway expenseGateway;
    private final TripGateway tripGateway;

    @Autowired
    public FlightTrackingService(FlightBookingRepository flightBookingRepository, ExpenseGateway expenseGateway, TripGateway tripGateway) {
        this.flightBookingRepository = flightBookingRepository;
        this.expenseGateway = expenseGateway;
        this.tripGateway = tripGateway;
    }

    @Transactional
    public FlightBooking saveFlightBooking(FlightBookingDTO dto) {
        FlightBooking booking = new FlightBooking();

        // Set flight details
        if(dto.getData().getBookingId() != null){
            booking.setBookingId(dto.getData().getBookingId());
        } else {
            booking.setBookingId(UUID.randomUUID());
        }
        booking.setFlightId(dto.getData().getFlightId());
        booking.setCarrier(dto.getData().getCarrier());
        booking.setFlightNumber(dto.getData().getFlightNumber());
        booking.setOrigin(dto.getData().getOrigin());
        booking.setDestination(dto.getData().getDestination());
        booking.setDistanceMiles(dto.getData().getDistanceMiles());
        booking.setTotalSeats(dto.getData().getTotalSeats());
        booking.setAvailableSeats(dto.getData().getAvailableSeats());
        booking.setAircraft(dto.getData().getAircraft());
        booking.setAirtime(dto.getData().getAirtime());
        booking.setDepartureDateTime(dto.getData().getDepartureDateTime());
        booking.setFlightPrice(dto.getData().getFlightPrice());

        FlightBooking savedBooking = flightBookingRepository.save(booking);

        if (dto.getAmountPaid() != null) {
            if (dto.getTripId() == null || dto.getResponsibleUserIds() == null || dto.getResponsibleUserIds().isEmpty()) {
                throw new IllegalArgumentException("TripId and responsibleUserIds are required when amountPaid is provided");
            }
            Long expenseId = expenseGateway.createExpense(dto.getAmountPaid(), dto.getTripId(), dto.getResponsibleUserIds());
            savedBooking.setExternalExpenseReference(expenseId);
            flightBookingRepository.save(savedBooking);
        }

        if (dto.getTripId() != null) {
            tripGateway.addToTrip(dto.getTripId(), savedBooking.getId());
        }

        return savedBooking;
    }
}
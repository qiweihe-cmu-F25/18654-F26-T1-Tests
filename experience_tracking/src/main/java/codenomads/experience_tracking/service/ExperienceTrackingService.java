package codenomads.experience_tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import codenomads.experience_tracking.domain.ExperienceBooking;
import codenomads.experience_tracking.dto.ExperienceBookingDTO;
import codenomads.experience_tracking.repository.ExperienceBookingRepository;
import jakarta.transaction.Transactional;
import codenomads.experience_tracking.gateway.ExpenseGateway;
import codenomads.experience_tracking.gateway.TripGateway;

import java.util.UUID;
import java.util.Set;

@Service
public class ExperienceTrackingService {

    private final ExperienceBookingRepository experienceBookingRepository;
    private final ExpenseGateway expenseGateway;
    private final TripGateway tripGateway;

    @Autowired
    public ExperienceTrackingService(ExperienceBookingRepository experienceBookingRepository, ExpenseGateway expenseGateway, TripGateway tripGateway) {
        this.experienceBookingRepository = experienceBookingRepository;
        this.expenseGateway = expenseGateway;
        this.tripGateway = tripGateway;
    }

    @Transactional
    public ExperienceBooking saveExperienceBooking(ExperienceBookingDTO dto) {
        ExperienceBooking booking = new ExperienceBooking();
        booking.setBookingId(dto.getBooking().getId());
        booking.setExperienceId(dto.getExperience().getId());
        booking.setStartDate(dto.getBooking().getStartDate());
        booking.setParticipants(dto.getBooking().getParticipants());

        // Set experience details
        booking.setOrganizer(dto.getExperience().getOrganizer());
        booking.setEventName(dto.getExperience().getEventName());
        booking.setAddress(dto.getExperience().getAddress());
        booking.setCity(dto.getExperience().getCity());
        booking.setState(dto.getExperience().getState());
        booking.setPostcode(dto.getExperience().getPostcode());
        booking.setBorough(dto.getExperience().getBorough());
        booking.setLatitude(dto.getExperience().getLatitude());
        booking.setLongitude(dto.getExperience().getLongitude());
        booking.setNta(dto.getExperience().getNta());
        booking.setCapacity(dto.getExperience().getCapacity());
        booking.setPricePerPerson(dto.getExperience().getPricePerPerson());

        ExperienceBooking savedBooking = experienceBookingRepository.save(booking);

        if (dto.getAmountPaid() != null) {
            if (dto.getTripId() == null || dto.getResponsibleUserIds() == null || dto.getResponsibleUserIds().isEmpty()) {
                throw new IllegalArgumentException("TripId and responsibleUserIds are required when amountPaid is provided");
            }
            Long expenseId = expenseGateway.createExpense(dto.getAmountPaid(), dto.getTripId(), dto.getResponsibleUserIds());
            savedBooking.setExternalExpenseReference(expenseId);
            experienceBookingRepository.save(savedBooking);
        }

        if (dto.getTripId() != null) {
            tripGateway.addToTrip(dto.getTripId(), savedBooking.getId());
        }

        return savedBooking;
    }

    public ExperienceBooking getExperienceById(Long id) {
        return experienceBookingRepository.findById(id).orElse(null);
    }
}
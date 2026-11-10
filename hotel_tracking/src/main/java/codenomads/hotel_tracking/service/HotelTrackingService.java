package codenomads.hotel_tracking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codenomads.hotel_tracking.domain.HotelBooking;
import codenomads.hotel_tracking.dto.HotelBookingDTO;
import codenomads.hotel_tracking.gateway.ExpenseGateway;
import codenomads.hotel_tracking.gateway.TripGateway;
import codenomads.hotel_tracking.repository.HotelBookingRepository;
import jakarta.transaction.Transactional;

@Service
public class HotelTrackingService {

    private final HotelBookingRepository hotelBookingRepository;
    private final ExpenseGateway expenseGateway;
    private final TripGateway tripGateway;

    @Autowired
    public HotelTrackingService(HotelBookingRepository hotelBookingRepository, ExpenseGateway expenseGateway, TripGateway tripGateway) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.expenseGateway = expenseGateway;
        this.tripGateway = tripGateway;
    }

    @Transactional
    public HotelBooking saveHotelBooking(HotelBookingDTO dto) {
        HotelBooking booking = new HotelBooking();
        // Set hotel details
        booking.setBookingId(dto.getData().getBooking().getId());
        booking.setHotelId(dto.getData().getHotel().getHotelId());
        booking.setBorocode(dto.getData().getHotel().getBorocode());
        booking.setBlock(dto.getData().getHotel().getBlock());
        booking.setLot(dto.getData().getHotel().getLot());
        booking.setStreetNumber(dto.getData().getHotel().getStreetNumber());
        booking.setStreetName(dto.getData().getHotel().getStreetName());
        booking.setZipcode(dto.getData().getHotel().getZipcode());
        booking.setBuildingClass(dto.getData().getHotel().getBuildingClass());
        booking.setOwnerName(dto.getData().getHotel().getOwnerName());
        booking.setBorough(dto.getData().getHotel().getBorough());
        booking.setLatitude(dto.getData().getHotel().getLatitude());
        booking.setLongitude(dto.getData().getHotel().getLongitude());
        booking.setArea(dto.getData().getHotel().getArea());
        
        // Set booking details
        booking.setStartDate(dto.getData().getBooking().getStartDate());
        booking.setEndDate(dto.getData().getBooking().getEndDate());
        booking.setOnePersonRooms(dto.getData().getBooking().getOnePersonRooms());
        booking.setTwoPersonRooms(dto.getData().getBooking().getTwoPersonRooms());
        booking.setFourPersonRooms(dto.getData().getBooking().getFourPersonRooms());

        HotelBooking savedBooking = hotelBookingRepository.save(booking);

        if (dto.getAmountPaid() != null) {
            if (dto.getTripId() == null || dto.getResponsibleUserIds() == null || dto.getResponsibleUserIds().isEmpty()) {
                throw new IllegalArgumentException("TripId and responsibleUserIds are required when amountPaid is provided");
            }
            Long expenseId = expenseGateway.createExpense(dto.getAmountPaid(), dto.getTripId(), dto.getResponsibleUserIds());
            savedBooking.setExternalExpenseReference(expenseId);
            hotelBookingRepository.save(savedBooking);
        }

        if (dto.getTripId() != null) {
            tripGateway.addToTrip(dto.getTripId(), savedBooking.getId());
        }

        return savedBooking;
    }
}
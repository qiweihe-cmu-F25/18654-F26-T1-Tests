package codenomads.hotel_tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import codenomads.hotel_tracking.dto.HotelBookingDTO;
import codenomads.hotel_tracking.service.HotelTrackingService;

@RestController
@RequestMapping("/hotel-tracking")
public class HotelTrackingController {

    private final HotelTrackingService hotelTrackingService;

    @Autowired
    public HotelTrackingController(HotelTrackingService hotelTrackingService) {
        this.hotelTrackingService = hotelTrackingService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Hotel tracking service is up and running");
    }

    @PostMapping({"", "/"})
    public ResponseEntity<String> receiveHotelBooking(@Valid @RequestBody HotelBookingDTO dto) {
        hotelTrackingService.saveHotelBooking(dto);
        return ResponseEntity.ok("Hotel booking received");
    }
}
package codenomads.flight_tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import codenomads.flight_tracking.dto.FlightBookingDTO;
import codenomads.flight_tracking.service.FlightTrackingService;

@RestController
@RequestMapping("/flight-tracking")
public class FlightTrackingController {

    private final FlightTrackingService flightTrackingService;

    @Autowired
    public FlightTrackingController(FlightTrackingService flightTrackingService) {
        this.flightTrackingService = flightTrackingService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Flight tracking service is up and running");
    }

    @PostMapping({"", "/"})
    public ResponseEntity<String> receiveFlightBooking(@Valid @RequestBody FlightBookingDTO dto) {
        flightTrackingService.saveFlightBooking(dto);
        return ResponseEntity.ok("Flight booking received");
    }
}
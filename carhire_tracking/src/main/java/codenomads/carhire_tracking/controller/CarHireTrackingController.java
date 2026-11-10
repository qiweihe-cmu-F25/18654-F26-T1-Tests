package codenomads.carhire_tracking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import codenomads.carhire_tracking.dto.CarHireBookingDTO;
import codenomads.carhire_tracking.service.CarHireTrackingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carhire-tracking")
public class CarHireTrackingController {

    private final CarHireTrackingService carHireTrackingService;

    @Autowired
    public CarHireTrackingController(CarHireTrackingService carHireTrackingService) {
        this.carHireTrackingService = carHireTrackingService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Car hire tracking service is up and running");
    }

    @PostMapping({"", "/"})
    public ResponseEntity<String> receiveCarHireBooking(@Valid @RequestBody CarHireBookingDTO dto) {
        carHireTrackingService.saveCarHireBooking(dto);
        return ResponseEntity.ok("Car hire booking received");
    }
}
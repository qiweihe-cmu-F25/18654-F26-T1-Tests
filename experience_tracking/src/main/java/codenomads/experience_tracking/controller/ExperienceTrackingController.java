package codenomads.experience_tracking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import codenomads.experience_tracking.dto.ExperienceBookingDTO;
import codenomads.experience_tracking.service.ExperienceTrackingService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/experience-tracking")
public class ExperienceTrackingController {

    private final ExperienceTrackingService experienceTrackingService;

    public ExperienceTrackingController(ExperienceTrackingService experienceTrackingService) {
        this.experienceTrackingService = experienceTrackingService;
    }

    @GetMapping({"", "/"})
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Experience tracking service is up and running");
    }
    
    @PostMapping({"", "/"})
    public ResponseEntity<String> receiveExperienceBooking(@Valid @RequestBody ExperienceBookingDTO dto) {
        experienceTrackingService.saveExperienceBooking(dto);
        return ResponseEntity.ok("Experience booking received");
    }
}
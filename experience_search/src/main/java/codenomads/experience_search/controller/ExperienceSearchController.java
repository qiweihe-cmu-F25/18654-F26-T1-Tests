package codenomads.experience_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;

import codenomads.experience_search.dto.ExperienceSearchCriteria;
import codenomads.experience_search.service.ExperienceSearchService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/experience-search")
public class ExperienceSearchController {

    private final ExperienceSearchService experienceSearchService;

    @Autowired
    public ExperienceSearchController(ExperienceSearchService experienceSearchService) {
        this.experienceSearchService = experienceSearchService;
    }

    @PostMapping("/search")
    public Mono<ResponseEntity<JsonNode>> searchExperiences(@RequestBody ExperienceSearchCriteria criteria) {
        return experienceSearchService.searchExperiences(criteria)
            .map(response -> ResponseEntity.ok(response));
    }

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Experience search service is running.");
    }
}
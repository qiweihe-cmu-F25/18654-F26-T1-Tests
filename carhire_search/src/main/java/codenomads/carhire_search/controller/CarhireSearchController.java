package codenomads.carhire_search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import codenomads.carhire_search.dto.CarhireSearchCriteria;
import codenomads.carhire_search.service.CarhireSearchService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/carhire-search")
public class CarhireSearchController {

    private final CarhireSearchService carhireSearchService;
    
    @Autowired
    public CarhireSearchController(CarhireSearchService carhireSearchService) {
        this.carhireSearchService = carhireSearchService;
    }

    @PostMapping("/search")
    public Mono<ResponseEntity<JsonNode>> searchCarhires(@Valid @RequestBody CarhireSearchCriteria criteria) {
        return carhireSearchService.searchCarhires(criteria)
            .map(response -> ResponseEntity.ok(response));
    }

    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Carhire search service is running.");
    }
}

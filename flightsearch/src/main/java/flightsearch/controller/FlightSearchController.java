package flightsearch.controller;

import flightsearch.dto.FlightSearchCriteria;
import flightsearch.dto.FlightSearchResponse;
import flightsearch.service.FlightSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/v1/flightsearch")
public class FlightSearchController {
    private final FlightSearchService flightSearchService;
    private static final Pattern IATA_CODE_PATTERN = Pattern.compile("^[A-Za-z]{3}$");

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @PostMapping("/flights")
    public ResponseEntity<?> searchFlights(@RequestBody FlightSearchCriteria criteria) {
        // Validate required fields
        String validationError = validateCriteria(criteria);
        if (validationError != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", validationError);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            // Normalize IATA codes to uppercase
            criteria.setDepartureAirport(criteria.getDepartureAirport().toUpperCase());
            criteria.setArrivalAirport(criteria.getArrivalAirport().toUpperCase());
            
            FlightSearchResponse response = this.flightSearchService.searchFlights(criteria);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal server error occurred while searching flights");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private String validateCriteria(FlightSearchCriteria criteria) {
        if (criteria.getDepartureAirport() == null || criteria.getDepartureAirport().trim().isEmpty()) {
            return "Invalid or missing departure_airport. Must be a 3-letter IATA code.";
        }
        if (!IATA_CODE_PATTERN.matcher(criteria.getDepartureAirport().trim()).matches()) {
            return "Invalid or missing departure_airport. Must be a 3-letter IATA code.";
        }

        if (criteria.getArrivalAirport() == null || criteria.getArrivalAirport().trim().isEmpty()) {
            return "Invalid or missing arrival_airport. Must be a 3-letter IATA code.";
        }
        if (!IATA_CODE_PATTERN.matcher(criteria.getArrivalAirport().trim()).matches()) {
            return "Invalid or missing arrival_airport. Must be a 3-letter IATA code.";
        }

        // Validate optional numeric fields
        if (criteria.getMaxPrice() != null && criteria.getMaxPrice() < 0) {
            return "max_price must be a positive number.";
        }
        if (criteria.getMinPrice() != null && criteria.getMinPrice() < 0) {
            return "min_price must be a positive number.";
        }
        if (criteria.getNumPassengers() != null && criteria.getNumPassengers() <= 0) {
            return "num_passengers must be a positive integer.";
        }

        return null; // No validation errors
    }
}
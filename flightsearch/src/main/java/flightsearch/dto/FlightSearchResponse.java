package flightsearch.dto;

import flightsearch.model.Flight;

import java.util.List;

public class FlightSearchResponse {
    private List<Flight> flights;

    public FlightSearchResponse() {}

    public FlightSearchResponse(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}

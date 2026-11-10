package userprofile.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import userprofile.model.Trip;

import java.util.List;

public class TripResponse {
    private List<Trip> trips;

    public TripResponse() { }

    public TripResponse(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> getTrips() {
        return trips;
    }
}

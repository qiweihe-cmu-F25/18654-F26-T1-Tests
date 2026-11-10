package flightsearch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class Flight {
    @JsonAlias("flightid")
    private String flightId;

    @JsonAlias("departure_datetime")
    private Instant departureDatetime;

    @JsonAlias("airtime")
    private Integer airtimeMinutes;

    @JsonAlias("flightnumber")
    private String flightNumber;

    @JsonAlias("distancemiles")
    private Integer distanceMiles;

    @JsonAlias("totalseats")
    private Integer totalSeats;

    @JsonAlias("availableseats")
    private Integer availableSeats;

    @JsonAlias("aircraft")
    private String aircraft;

    @JsonAlias("flight_price")
    private Double flightPrice;

    private String origin;
    private String destination;
    private String carrier;

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public void setDepartureDatetime(Instant departureDatetime) {
        this.departureDatetime = departureDatetime;
    }

    public void setAirtimeMinutes(Integer airtimeMinutes) {
        this.airtimeMinutes = airtimeMinutes;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public void setDistanceMiles(Integer distanceMiles) {
        this.distanceMiles = distanceMiles;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public void setFlightPrice(Double flightPrice) {
        this.flightPrice = flightPrice;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Flight() { }

    public Flight(String flightId, Instant departureDatetime, Integer airtimeMinutes, String flightNumber, Integer distanceMiles, Integer totalSeats, Integer availableSeats, String aircraft, Double flightPrice, String origin, String destination, String carrier) {
        this.flightId = flightId;
        this.departureDatetime = departureDatetime;
        this.airtimeMinutes = airtimeMinutes;
        this.flightNumber = flightNumber;
        this.distanceMiles = distanceMiles;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.aircraft = aircraft;
        this.flightPrice = flightPrice;
        this.origin = origin;
        this.destination = destination;
        this.carrier = carrier;
    }

    public String getFlightId() {
        return flightId;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]'Z'", timezone = "UTC")
    public Instant getDepartureDatetime() {
        return departureDatetime;
    }

    public Integer getAirtimeMinutes() {
        return airtimeMinutes;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public Integer getDistanceMiles() {
        return distanceMiles;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public String getAircraft() {
        return aircraft;
    }

    public Double getFlightPrice() {
        return flightPrice;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getCarrier() {
        return carrier;
    }
}

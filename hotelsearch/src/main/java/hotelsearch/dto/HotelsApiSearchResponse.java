package hotelsearch.dto;

import hotelsearch.model.Hotel;

import java.util.List;

public class HotelsApiSearchResponse {
    private List<Hotel> hotels;

    public HotelsApiSearchResponse() {}

    public HotelsApiSearchResponse(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }
}

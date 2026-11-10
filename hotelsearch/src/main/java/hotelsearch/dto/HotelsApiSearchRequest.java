package hotelsearch.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hotelsearch.model.PriceRange;
import hotelsearch.model.RoomType;

import java.sql.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelsApiSearchRequest {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    private Map<RoomType, Integer> requestedRooms;

    private Map<RoomType, PriceRange> priceRange;

    private String hotelId;

    private String name;

    private Double longitude;

    private Double latitude;

    private Double radius;

    public HotelsApiSearchRequest() { }

    public HotelsApiSearchRequest(Date startDate, Date endDate, Map<RoomType, Integer> requestedRooms, Map<RoomType, PriceRange> priceRange, String hotelId, String name, Double longitude, Double latitude, Double radius) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestedRooms = requestedRooms;
        this.priceRange = priceRange;
        this.hotelId = hotelId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Map<RoomType, Integer> getRequestedRooms() {
        return requestedRooms;
    }

    public Map<RoomType, PriceRange> getPriceRange() {
        return priceRange;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setRequestedRooms(Map<RoomType, Integer> requestedRooms) {
        this.requestedRooms = requestedRooms;
    }

    public void setPriceRange(Map<RoomType, PriceRange> priceRange) {
        this.priceRange = priceRange;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getName() {
        return name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getRadius() {
        return radius;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}

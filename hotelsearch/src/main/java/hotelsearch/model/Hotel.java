package hotelsearch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Map;

public class Hotel {
    @JsonAlias("hotel_id")
    private String hotelId;

    @JsonAlias("borocode")
    private String boroughCode;

    @JsonAlias("block")
    private String block;

    @JsonAlias("lot")
    private String lot;

    @JsonAlias("street_number")
    private String streetNumber;

    @JsonAlias("street_name")
    private String streetName;

    @JsonAlias("zipcode")
    private Integer zipcode;

    @JsonAlias("building_class")
    private String buildingClass;

    @JsonAlias("owner_name")
    private String ownerName;

    @JsonAlias("borough")
    private String borough;

    @JsonAlias("hotel_rooms")
    private Map<RoomRate, Integer> roomRates;

    private Double latitude;
    private Double longitude;
    private String area;

    public Hotel() { }

    public Hotel(String hotelId, String boroughCode, String block, String lot, String streetNumber, String streetName, Integer zipcode, String buildingClass, String ownerName, String borough, Map<RoomRate, Integer> roomRates, Double latitude, Double longitude, String area) {
        this.hotelId = hotelId;
        this.boroughCode = boroughCode;
        this.block = block;
        this.lot = lot;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.zipcode = zipcode;
        this.buildingClass = buildingClass;
        this.ownerName = ownerName;
        this.borough = borough;
        this.roomRates = roomRates;
        this.latitude = latitude;
        this.longitude = longitude;
        this.area = area;
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getBoroughCode() {
        return boroughCode;
    }

    public String getBlock() {
        return block;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public String getBuildingClass() {
        return buildingClass;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getBorough() {
        return borough;
    }

    public Map<RoomRate, Integer> getRoomRates() {
        return roomRates;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getArea() {
        return area;
    }

    public String getLot() {
        return lot;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getAddress() {
        return "Lot " + lot + ", Block " + block + "\n" + streetNumber + ", " + streetName + "\n" + zipcode;
    }
}

package codenomads.tripmanagement.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateTripRequestTest {

    private CreateTripRequest createTripRequest;
    private Date testStartDate;
    private Date testEndDate;

    @BeforeEach
    void setUp() {
        createTripRequest = new CreateTripRequest();
        testStartDate = new Date();
        testEndDate = new Date(System.currentTimeMillis() + 86400000);
    }

    @Test
    void setAndGetTripName_ShouldWorkCorrectly() {
        String expectedTripName = "Amazing European Tour 2024";
        createTripRequest.setTripName(expectedTripName);
        
        assertEquals(expectedTripName, createTripRequest.getTripName());
    }

    @Test
    void setAndGetStartDate_ShouldWorkCorrectly() {
        createTripRequest.setStartDate(testStartDate);
        
        assertEquals(testStartDate, createTripRequest.getStartDate());
    }

    @Test
    void tripNameSpecialCharactersShouldBeAllowed() {
        String specialName = "Trip with Ümlauts & Special Ch@racters! 2024 🌍";
        createTripRequest.setTripName(specialName);
        
        assertEquals(specialName, createTripRequest.getTripName());
    }

}
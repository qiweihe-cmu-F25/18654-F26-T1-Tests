package hotelsearch.controller;

import hotelsearch.dto.HotelsApiSearchRequest;
import hotelsearch.dto.HotelsApiSearchResponse;
import hotelsearch.service.HotelSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelSearchControllerTest {

    @Mock
    private HotelSearchService hotelSearchService;

    @InjectMocks
    private HotelSearchController hotelSearchController;

    @Test
    void getHotelsNegativeRadiusShouldReturnEmptyListWithoutCallingService() {
        HotelsApiSearchRequest request = new HotelsApiSearchRequest(
            null, null, null, null, null, null, null, null, -1.0);

        ResponseEntity<HotelsApiSearchResponse> response = hotelSearchController.getHotels(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getHotels());
        assertTrue(response.getBody().getHotels().isEmpty());
        verify(hotelSearchService, never()).getHotels(any());
    }

    @Test
    void getHotelsValidRadiusShouldDelegateToService() {
        HotelsApiSearchRequest request = new HotelsApiSearchRequest(
            null, null, null, null, null, null, null, null, 10.0);
        HotelsApiSearchResponse serviceResponse = new HotelsApiSearchResponse(java.util.List.of());
        when(hotelSearchService.getHotels(request)).thenReturn(serviceResponse);

        ResponseEntity<HotelsApiSearchResponse> response = hotelSearchController.getHotels(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(serviceResponse, response.getBody());
        verify(hotelSearchService, times(1)).getHotels(request);
    }
}

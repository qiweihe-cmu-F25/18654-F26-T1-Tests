package hotelsearch.service;

import hotelsearch.dto.HotelsApiSearchRequest;
import hotelsearch.dto.HotelsApiSearchResponse;
import hotelsearch.gateway.HotelsApiGateway;
import hotelsearch.repository.HotelSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class HotelSearchService {

    private final HotelSearchRepository hotelSearchRepository;
    private final HotelsApiGateway hotelsApiGateway;

    @Autowired
    public HotelSearchService(HotelSearchRepository hotelSearchRepository, HotelsApiGateway hotelsApiGateway) {
        this.hotelSearchRepository = hotelSearchRepository;
        this.hotelsApiGateway = hotelsApiGateway;
    }

    public HotelsApiSearchResponse getHotels(HotelsApiSearchRequest hotelSearchRequest) {
        try {
            return this.hotelsApiGateway.getFromHotelSearchService(hotelSearchRequest).block();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return new HotelsApiSearchResponse(new ArrayList<>());
    }
}
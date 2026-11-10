package codenomads.experience_tracking.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.http.HttpHeaders;

@Component
public class TripGateway {

    private final WebClient webClient;

    @Autowired
    public TripGateway(WebClient.Builder webClientBuilder, @Value("${external.trip.url}") String tripServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(tripServiceUrl).build();
    }

    public void addToTrip(Long tripId, Long bookingId) {
        TripBookingRequest request = new TripBookingRequest(bookingId);

        String token = (String) RequestContextHolder.getRequestAttributes()
            .getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);

        webClient.post()
            .uri("/trips/{tripId}/bookings", tripId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    @Data
    @ToString
    public static class TripBookingRequest {
        private Long bookingId;
        private String source;

        public TripBookingRequest(Long bookingId) {
            this.bookingId = bookingId;
            this.source = "EXPERIENCE";
        }
    }
}
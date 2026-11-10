package userprofile.gateway;

import userprofile.dto.TripRequest;
import userprofile.dto.TripResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TripManagementGateway {
    @Value("${external.trips.url}")
    private String tripServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(tripServiceUrl)
                .build();
    }

    public Mono<TripResponse> getTrips(TripRequest request) {
        return webClient.get()
                .uri("/trips")
                .retrieve()
                .bodyToMono(TripResponse.class);
    }
}

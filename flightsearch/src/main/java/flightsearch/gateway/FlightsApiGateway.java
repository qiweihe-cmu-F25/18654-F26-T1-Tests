package flightsearch.gateway;

import flightsearch.dto.FlightSearchCriteria;
import flightsearch.dto.FlightSearchResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FlightsApiGateway {
    @Value("${external.flight-search.url}")
    private String flightSearchServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(flightSearchServiceUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public Mono<FlightSearchResponse> searchFlightsOnApi(FlightSearchCriteria criteria) {
        return this.webClient.post()
                .uri("")
                .bodyValue(criteria)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<FlightSearchResponse>() {});
    }
}
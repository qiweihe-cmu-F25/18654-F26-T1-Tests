package hotelsearch.gateway;

import hotelsearch.dto.HotelsApiSearchRequest;
import hotelsearch.dto.HotelsApiSearchResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HotelsApiGateway {
    @Value("${external.hotel-search.url}")
    private String hotelSearchServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(hotelSearchServiceUrl)
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    public Mono<HotelsApiSearchResponse> getFromHotelSearchService(HotelsApiSearchRequest request) {
        return this.webClient.post()
                .uri("")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<HotelsApiSearchResponse>() {});
    }
}
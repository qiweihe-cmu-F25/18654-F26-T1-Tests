package codenomads.experience_search.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.JsonNode;

import codenomads.experience_search.dto.ExperienceSearchCriteria;
import codenomads.experience_search.exceptions.CustomException;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Component
public class ExperienceSearchGateway {

    @Value("${external.experience-search.provider-url}")
    private String providerUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        // Built independently of the app's Spring-managed WebClient.Builder so this
        // client's JSON (de)serialization is unaffected by our own naming strategy
        // config and always matches the external provider's own camelCase contract.
        this.webClient = WebClient.builder()
                .baseUrl(providerUrl)
                .build();
    }

    public Mono<JsonNode> searchExperiences(ExperienceSearchCriteria criteria) {
        return webClient.post()
            .uri("")
            .bodyValue(criteria)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .onErrorMap(WebClientResponseException.class, e -> {
                // Propagate the provider's own 4xx; anything else (5xx, undecodable body) is a bad gateway.
                if (e.getStatusCode().is4xxClientError()) {
                    return new CustomException("Experience provider rejected the request: " + e.getStatusText(),
                            e.getStatusCode().value());
                }
                return new CustomException("Experience provider returned an error", 502);
            })
            .onErrorMap(WebClientRequestException.class,
                    e -> new CustomException("Experience provider is unavailable", 503));
    }
}
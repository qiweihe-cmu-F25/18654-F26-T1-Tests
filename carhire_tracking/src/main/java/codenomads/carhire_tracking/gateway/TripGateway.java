package codenomads.carhire_tracking.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import com.fasterxml.jackson.databind.JsonNode;
import codenomads.carhire_tracking.exceptions.CustomException;
import reactor.core.publisher.Mono;

@Component
public class TripGateway {

    private final WebClient webClient;

    @Autowired
    public TripGateway(WebClient.Builder webClientBuilder, @Value("${external.trip.url}") String tripServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(tripServiceUrl).build();
    }

    /**
     * Asks trip management whether the caller may add a booking to the trip. Called before any
     * side effects (booking, expense) so a rejected caller cannot leave partial state behind.
     * The rule itself lives in trip management; a rejection is relayed as-is.
     */
    public void assertUserCanAddBookingToTrip(Long tripId) {
        withErrorMapping(
            webClient.get()
                .uri("/trips/{tripId}/can-add-booking", tripId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + currentToken())
                .retrieve(),
            "Trip request rejected")
            .bodyToMono(Void.class)
            .onErrorMap(WebClientRequestException.class,
                e -> new CustomException("Trip management service is unavailable", 503))
            .block();
    }

    public void addToTrip(Long tripId, Long bookingId) {
        TripBookingRequest request = new TripBookingRequest(bookingId);

        System.out.println("request: " + request);

        withErrorMapping(
            webClient.post()
                .uri("/trips/{tripId}/bookings", tripId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + currentToken())
                .bodyValue(request)
                .retrieve(),
            "Trip request rejected")
            .bodyToMono(Void.class)
            .onErrorMap(WebClientRequestException.class,
                e -> new CustomException("Trip management service is unavailable", 503))
            .block();
    }

    private String currentToken() {
        return (String) RequestContextHolder.getRequestAttributes()
            .getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * Propagates the trip service's own 4xx status and message (falling back to
     * {@code fallbackMessage} when it sent no body); any 5xx is reported as a bad gateway.
     */
    private WebClient.ResponseSpec withErrorMapping(WebClient.ResponseSpec spec, String fallbackMessage) {
        return spec
            .onStatus(status -> status.is4xxClientError(), response -> toCustomException(response, fallbackMessage))
            .onStatus(status -> status.is5xxServerError(),
                response -> Mono.just(new CustomException("Trip management service returned an error", 502)));
    }

    private Mono<CustomException> toCustomException(ClientResponse response, String fallbackMessage) {
        int status = response.statusCode().value();
        return response.bodyToMono(JsonNode.class)
            .map(body -> new CustomException(
                body.hasNonNull("message") ? body.get("message").asText() : fallbackMessage, status))
            .defaultIfEmpty(new CustomException(fallbackMessage, status));
    }

    @Data
    @ToString
    public static class TripBookingRequest {
        private Long bookingId;
        private String source;

        public TripBookingRequest(Long bookingId) {
            this.bookingId = bookingId;
            this.source = "CARHIRE";
        }
    }
}
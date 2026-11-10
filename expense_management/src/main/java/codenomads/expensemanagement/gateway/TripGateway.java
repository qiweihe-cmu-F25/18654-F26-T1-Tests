package codenomads.expensemanagement.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import codenomads.expensemanagement.exceptions.CustomException;

@Component
public class TripGateway {

    private final WebClient webClient;

    @Autowired
    public TripGateway(WebClient.Builder webClientBuilder, @Value("${external.trip.url}") String tripServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(tripServiceUrl).build();
    }

    public boolean isUserInTrip(Long tripId, Long userId) {
        String token = (String) RequestContextHolder.getRequestAttributes()
            .getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);

        try {
            Boolean result = webClient.get()
                .uri("/trips/{tripId}/members/{userId}", tripId, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
            return Boolean.TRUE.equals(result);
        } catch (WebClientResponseException e) {
            HttpStatusCode status = e.getStatusCode();
            if (status.value() == 404) {
                throw new CustomException("Trip not found", 404);
            }
            if (status.value() == 401 || status.value() == 403) {
                throw new CustomException("User is not a member of the trip", 400);
            }
            throw new CustomException("Unable to verify trip membership", 400);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Unable to verify trip membership", 400);
        }
    }
}

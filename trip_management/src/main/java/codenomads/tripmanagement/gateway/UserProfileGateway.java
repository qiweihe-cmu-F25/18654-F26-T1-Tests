package codenomads.tripmanagement.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import codenomads.tripmanagement.exception.CustomException;

@Component
public class UserProfileGateway {

    private final WebClient webClient;

    @Autowired
    public UserProfileGateway(
            WebClient.Builder webClientBuilder,
            @Value("${external.userprofile.url:http://localhost:18081}") String userProfileServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(userProfileServiceUrl).build();
    }

    public void ensureUserExists(Long userId) {
        if (userId == null) {
            throw new CustomException("User id is required", 400);
        }

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new CustomException("Unable to verify user", 400);
        }
        String token = (String) requestAttributes.getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);

        try {
            webClient.get()
                .uri("/api/v1/userprofile/{id}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toBodilessEntity()
                .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                throw new CustomException("User not found", 404);
            }
            throw new CustomException("Unable to verify user", 400);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException("Unable to verify user", 400);
        }
    }
}

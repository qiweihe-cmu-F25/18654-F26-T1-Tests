package userprofile.gateway;

import userprofile.dto.ExpenseRequest;
import userprofile.dto.ExpenseResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class ExpenseManagementGateway {

    @Value("${external.expenses.url}")
    private String expenseServiceUrl;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.builder()
                .baseUrl(expenseServiceUrl)
                .build();
    }

    public Mono<ExpenseResponse> getExpenses(ExpenseRequest request) {
        return webClient.get()
                .uri("/expenses")
                .retrieve()
                .bodyToMono(ExpenseResponse.class);
    }

}

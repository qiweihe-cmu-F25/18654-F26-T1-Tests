package codenomads.hotel_tracking.gateway;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.http.HttpHeaders;
import lombok.Data;
import lombok.ToString;

@Component
public class ExpenseGateway {

    private final WebClient webClient;

    @Autowired
    public ExpenseGateway(WebClient.Builder webClientBuilder, @Value("${external.expense.url}") String expenseServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(expenseServiceUrl).build();
    }

    public Long createExpense(BigDecimal amount, Long tripId, Set<Long> responsibleUserIds) {
        Long createdByUserId = (Long) RequestContextHolder.getRequestAttributes()
            .getAttribute("userid", RequestAttributes.SCOPE_REQUEST);
        
        ExpenseRequest request = new ExpenseRequest(amount, tripId, responsibleUserIds, createdByUserId);

        String token = (String) RequestContextHolder.getRequestAttributes()
            .getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);

        ExpenseResponse response = webClient.post()
            .uri("/expenses")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(ExpenseResponse.class)
            .block();

        return response != null ? response.getExpenseId() : null;
    }

    @Data
    @ToString
    public static class ExpenseRequest {
        private BigDecimal amount;
        private Long tripId;
        private Set<Long> responsibleUserIds;
        private String source = "HOTEL";
        private Long createdByUserId;

        public ExpenseRequest(BigDecimal amount, Long tripId, Set<Long> responsibleUserIds, Long createdByUserId) {
            this.amount = amount;
            this.tripId = tripId;
            this.responsibleUserIds = responsibleUserIds;
            this.createdByUserId = createdByUserId;
        }
    }

    @Data
    @ToString
    public static class ExpenseResponse {
        private Long expenseId;
    }
}
package codenomads.carhire_search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import codenomads.carhire_search.dto.CarhireSearchCriteria;
import codenomads.carhire_search.gateway.CarhireSearchGateway;
import reactor.core.publisher.Mono;

@Service
public class CarhireSearchService {

    private final CarhireSearchGateway carhireSearchGateway;

    @Autowired
    public CarhireSearchService(CarhireSearchGateway carhireSearchGateway) {
        this.carhireSearchGateway = carhireSearchGateway;
    }

    public Mono<JsonNode> searchCarhires(CarhireSearchCriteria criteria) {
        return carhireSearchGateway.searchCarhires(criteria);
    }
}
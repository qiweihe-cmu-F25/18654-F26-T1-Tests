package codenomads.experience_search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import codenomads.experience_search.dto.ExperienceSearchCriteria;
import codenomads.experience_search.gateway.ExperienceSearchGateway;
import reactor.core.publisher.Mono;

@Service
public class ExperienceSearchService {

    private final ExperienceSearchGateway experienceSearchGateway;

    @Autowired
    public ExperienceSearchService(ExperienceSearchGateway experienceSearchGateway) {
        this.experienceSearchGateway = experienceSearchGateway;
    }

    public Mono<JsonNode> searchExperiences(ExperienceSearchCriteria criteria) {
        return experienceSearchGateway.searchExperiences(criteria);
    }
}
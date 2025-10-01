package io.dbeauregard.pivotalspring_client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Configuration
class PivotalSpringRestClient {

    private static final Logger log = LoggerFactory.getLogger(PivotalSpringRestClient.class);

    private final RestClient restClient;

    PivotalSpringRestClient(@Value("${io.dbeauregard.pivotalspring.baseurl}") String baseUrl,
            RestClient.Builder rcBuilder) {
        log.info("Pivotal Spring Base URL: {}", baseUrl);
        restClient = rcBuilder.baseUrl(baseUrl).build();
    }

    //For Unit Tests
    @Bean
    RestClient getRestClient() {
        return restClient;
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner run() throws Exception {
        return _ -> {
            getOne(Long.valueOf(1));
            getAll();
            System.exit(0);
        };
    }

    public House getOne(Long id) {
        House result = restClient.get().uri("/houses/{id}", id).retrieve().body(House.class);
        log.info("House 1: {}", result);
        return result;
    }

    public List<House> getAll() {
        CollectionModel<EntityModel<House>> result = restClient.get().uri("/houses").retrieve()
                .body(new ParameterizedTypeReference<CollectionModel<EntityModel<House>>>() {
                });
        List<House> houseList = new ArrayList<House>();

        if (result == null) {
            log.info("Received NULL Results for getAll houses");
            return houseList;
        }

        result.getContent().forEach(em -> {
            if (em != null) {
                log.info("House: {}", em.getContent());
                houseList.add(em.getContent());
            }
        });
        return houseList;
    }
}

package io.dbeauregard.pivotalspring_client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
public class PivotalSpringRestClient {

    private static final Logger log = LoggerFactory.getLogger(PivotalSpringRestClient.class);

    @Value("${io.dbeauregard.pivotalspring.url}")
    private String url;

    private final RestTemplate restTemplate;

    PivotalSpringRestClient(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return restTemplate;
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner run() throws Exception {
        return args -> {
            log.info("Pivotal Spring URL: {}", url);
            getOne(Long.valueOf(1));
            getAll();
            System.exit(0);
        };
    }

    public List<House> getAll() {
        // House[] results = restTemplate.getForObject(url + "/houses", House[].class);
        ResponseEntity<List<House>> response = restTemplate.exchange(
                url + "/houses",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<House>>() {
                });
        List<House> results = response.getBody();
        results.forEach(h -> log.info(h.toString()));
        return results;
    }

    public House getOne(Long id) {
        House result = restTemplate.getForObject(url + "/house/" + id, House.class);
        log.info(result.toString());
        return result;
    }
}

package io.dbeauregard.pivotalspring_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
public class RestClient {

    private static final Logger log = LoggerFactory.getLogger(RestClient.class);

    @Value("${io.dbeauregard.pivotalspring.url}")
    private String url;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {

            log.info("Pivotal Spring URL: {}", url);
            House result = restTemplate.getForObject(url + "/house/1", House.class);
            log.info(result.toString());

            House[] results = restTemplate.getForObject(url + "/houses", House[].class);
            for (House h : results) {
                log.info(h.toString());
            }
        };
    }
}

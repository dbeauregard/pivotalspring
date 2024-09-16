package io.dbeauregard.pivotalspring_client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@Profile("!test")
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			
            House result = restTemplate.getForObject("http://localhost:8080/house/1", House.class);
            log.info(result.toString());

            House[] results = restTemplate.getForObject("http://localhost:8080/houses", House[].class);
            for (House h : results) {
                log.info(h.toString());   
            }
		};
	}
}

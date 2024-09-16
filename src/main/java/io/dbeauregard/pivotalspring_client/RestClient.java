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
			// Quote quote = restTemplate.getForObject(
			// 		"http://localhost:8080/api/random", Quote.class);
			var result = restTemplate.getForObject("http://localhost:8080/houses", String.class);
            log.info(result.toString());
		};
	}
}

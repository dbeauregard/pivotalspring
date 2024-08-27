package io.dbeauregard.pivotalspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PivotalspringApplication {

	private static Logger log = LoggerFactory.getLogger(PivotalspringApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PivotalspringApplication.class, args);
	}

	@Bean
	public CommandLineRunner test (HouseRepository repo) {
		return (args) -> {
			log.info("testing repo");
			repo.save(new HouseEntity("1234 street", Integer.valueOf(2)));
		};
	}

}

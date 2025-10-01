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
		log.info("Hello World from the Spring Boot main method!");
	}

	@Bean
	public CommandLineRunner test (HouseRepository repo) {
		return _ -> {
			log.info("Hello World from the Command Line Runner!");
			log.info("Testing Database Repository with 1 add");
			repo.save(new HouseEntity("1234 Seasame Street", 987000, 3, 2, 1500));
			log.info("Reading Database Repository:");
			repo.findAll().forEach(house -> {
				log.info("Found House: {}", house);
			});
		};
	}

}

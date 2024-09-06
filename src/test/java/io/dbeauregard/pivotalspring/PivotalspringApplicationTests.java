package io.dbeauregard.pivotalspring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PivotalspringApplicationTests {

	@Autowired
	private HouseController controller;

	@Autowired
	private HouseRepository repo;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;


	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(repo).isNotNull();
	}

	@Test
	void actuatorHealthy() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/actuator/health",
				String.class)).contains("\"status\":\"UP\"");
	}

}

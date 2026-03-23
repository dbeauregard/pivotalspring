package io.dbeauregard.pivotalspring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import io.dbeauregard.pivotalspring.restapi.HouseController;

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PivotalspringApplicationTests {

	@Autowired
	private HouseController controller;

	@Autowired
	private HouseRepository repo;

	@LocalServerPort
	private int port;

	@Autowired
  	private RestTestClient restClient;

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(repo).isNotNull();
	}

	@Test
	void actuatorHealthy() {

		this.restClient.get().uri("http://localhost:" + port + "/actuator/health").exchange()
												.expectStatus().is2xxSuccessful()
												.expectBody().jsonPath("$.status").isEqualTo("UP");
				
	}

}

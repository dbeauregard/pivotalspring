package io.dbeauregard.pivotalspring_client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class PivotalspringClientApplicationTests {

	@Autowired
	RestClient restClient;

	@Test
	public void contextLoads() {
		assertNotNull(restClient);
	}

}

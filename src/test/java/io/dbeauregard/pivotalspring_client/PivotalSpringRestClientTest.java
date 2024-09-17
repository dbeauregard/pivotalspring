package io.dbeauregard.pivotalspring_client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestClientTest(PivotalSpringRestClient.class)
@ActiveProfiles(profiles = "test")
public class PivotalSpringRestClientTest {

  @Autowired
  private PivotalSpringRestClient client;

  @Autowired
  private MockRestServiceServer server;

  @Autowired
  private ObjectMapper objectMapper;

  private Long testId = Long.valueOf(1);
  private String testAddress = "1234 Street";
  private Integer testPrice = 199;

  @BeforeEach
  public void beforeEach() {
    server.reset();
  }

  @AfterEach
  public void afterEach() {
    server.verify();
  }

  @Test
  public void testGetOne() throws Exception {
    String detailsString = objectMapper.writeValueAsString(new House(testId, testAddress, testPrice));

    server.expect(ExpectedCount.once(), requestTo("http://localhost:8080/house/1"))
        .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

    House result = client.getOne(Long.valueOf(1));

    assertThat(result.id()).isEqualTo(testId);
    assertThat(result.address()).isEqualTo(testAddress);
    assertThat(result.price()).isEqualTo(testPrice);
  }

  @Test
  public void testGetAll() throws Exception {
    List<House> houses = new ArrayList<House>();
    houses.add(new House(testId, testAddress, testPrice));
    houses.add(new House(Long.valueOf(testId+1), testAddress, testPrice));
    houses.add(new House(Long.valueOf(testId+2), testAddress, testPrice));
    String detailsString = objectMapper.writeValueAsString(houses);

    server.expect(ExpectedCount.once(), requestTo("http://localhost:8080/houses"))
        .andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

    House [] results = client.getAll();
    assertThat(results.length).isEqualTo(3);
    assertThat(results[1].id()).isEqualTo(Long.valueOf(testId+1));
  }

}

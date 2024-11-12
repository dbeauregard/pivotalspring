package io.dbeauregard.pivotalspring.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dbeauregard.pivotalspring.HouseEntity;

@SpringBootTest
@ActiveProfiles(profiles = "psql")
@AutoConfigureMockMvc
@Testcontainers
public class TestContainersTest {

    private static final String jsonListPrefix = "$._embedded.houseList";

    @Autowired
    private MockMvc mockMvc;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/houses")).andDo(print())
                .andExpect(status().isOk())
                // .andExpect(jsonPath("$", hasSize(4))) //tests are run in parallel so this
                // changes
                .andExpect(jsonPath(jsonListPrefix + ".[0].id").value(1))
                .andExpect(jsonPath(jsonListPrefix + ".[0].address").isNotEmpty())
                .andExpect(jsonPath(jsonListPrefix + ".[0].price").isNotEmpty());
    }

    @Test
    public void testAdd() throws Exception {
        mockMvc.perform(post("/houses")
                .content(asJsonString(new HouseEntity("1234", 9876, 3, 3, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());
    }

    // TODO: is this necessary, or is there a better way?
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

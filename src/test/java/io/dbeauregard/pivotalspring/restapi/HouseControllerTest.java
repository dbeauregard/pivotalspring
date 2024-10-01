package io.dbeauregard.pivotalspring.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dbeauregard.pivotalspring.HouseEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class HouseControllerTest {

    private static final String jsonListPrefix = "$._embedded.houseList";

    @Autowired
    private MockMvc mockMvc;

    //TODO: Mock Database or use fixed test data...
    // @Mock
    // private HouseRepository mockrepo;

    // @InjectMocks
    // private HouseController controller;

    @Test
    public void testGetOneValid() throws Exception {
        mockMvc.perform(get("/houses/1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());
    }

    @Test
    // Invalid ID -> 404
    public void testGetOneInValid() throws Exception {
        mockMvc.perform(get("/houses/404")).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/houses")).andDo(print())
                .andExpect(status().isOk())
                // .andExpect(jsonPath("$", hasSize(4))) //tests are run in parallel so this changes
                .andExpect(jsonPath(jsonListPrefix + ".[0].id").value(1))
                .andExpect(jsonPath(jsonListPrefix + ".[0].address").isNotEmpty())
                .andExpect(jsonPath(jsonListPrefix + ".[0].price").isNotEmpty());
    }

    @Test
    public void testAdd() throws Exception {
        mockMvc.perform(post("/houses")
                .content(asJsonString(new HouseEntity("1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());
    }

    @Test
    public void testUpdateNewFails() throws Exception {
        mockMvc.perform(put("/houses/20")
                .content(asJsonString(new HouseEntity(Long.valueOf(20), "1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateExisting() throws Exception {
        mockMvc.perform(put("/houses/2")
                .content(asJsonString(new HouseEntity(Long.valueOf(2), "1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());
    }

    @Test
    // Id in Body is null
    public void testUpdateNullId() throws Exception {
        mockMvc.perform(put("/houses/2")
                .content(asJsonString(new HouseEntity("1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    // Id in Body doesn't match parameter
    public void testUpdateWrongID() throws Exception {
        mockMvc.perform(put("/houses/2")
                .content(asJsonString(new HouseEntity(Long.valueOf(99), "1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteValid() throws Exception {
        mockMvc.perform(delete("/houses/4"))
        .andDo(print()).andExpect(status().isOk());

        //Should no longer exist
        mockMvc.perform(get("/houses/4")).andDo(print())
        .andExpect(status().isNotFound());
    }

    @Test
    // Invalid ID -> 404
    public void testDeleteInvalid() throws Exception {
        mockMvc.perform(delete("/houses/99"))
        .andDo(print()).andExpect(status().isNotFound());
    }

    //TODO: is this necessary, or is there a better way?
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

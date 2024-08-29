package io.dbeauregard.pivotalspring;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class HouseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HouseRepository mockrepo;
    
    @InjectMocks
    private HouseController controller;

    @Test
    public void testGetOneValid() throws Exception {
        mockMvc.perform(get("/house/1")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    // Invalid ID -> 404
    public void testGetOneInValid() throws Exception {

    }

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testUpdateNew() throws Exception {

    }

    @Test
    public void testUpdateExisting() throws Exception {

    }

    @Test
    // Id in Body is null
    public void testUpdateNullId() throws Exception {

    }

    @Test
    // Id in Body doesn't match parameter
    public void testUpdateWrongID() throws Exception {

    }

    @Test
    public void testDeleteValid() throws Exception {

    }

    @Test
    // Invalid ID -> 404
    public void testDeleteInvalid() throws Exception {

    }

}

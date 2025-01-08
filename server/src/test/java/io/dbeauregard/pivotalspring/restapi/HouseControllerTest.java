package io.dbeauregard.pivotalspring.restapi;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.dbeauregard.pivotalspring.HouseEntity;
import io.dbeauregard.pivotalspring.HouseRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class HouseControllerTest {

    private static final String jsonListPrefix = "$._embedded.houseList";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HouseRepository mockrepo;

    @Before
    public void setup() {
        reset(mockrepo);
    }

    @Test
    public void testGetOneValid() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(1);
        when(mockrepo.findById(id)).thenReturn(Optional.of(createTestHouse(id)));

        // Run Test
        mockMvc.perform(get("/houses/" + id.toString())).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());

        // Verify Mock
        verify(mockrepo, times(1)).findById(id);
        // verifyNoMoreInteractions(mockrepo);  //Not recomended to overuse
    }

    @Test
    // Invalid ID -> 404
    public void testGetOneNotFound() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(404);
        when(mockrepo.findById(id)).thenThrow(new HouseNotFoundException(id));

        // Run Test
        mockMvc.perform(get("/houses/" + id.toString())).andDo(print())
                .andExpect(status().isNotFound());

        // Verify Mock
        verify(mockrepo, times(1)).findById(id);
    }

    @Test
    public void testGetAll() throws Exception {
        // Mock Stubs
        int size = 5;
        when(mockrepo.findAll()).thenReturn(createTestHouseList(size));

        // Run Test
        mockMvc.perform(get("/houses")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(jsonListPrefix, hasSize(size)))
                .andExpect(jsonPath(jsonListPrefix + ".[1].id").value(1))
                .andExpect(jsonPath(jsonListPrefix + ".[1].address").isNotEmpty())
                .andExpect(jsonPath(jsonListPrefix + ".[1].price").isNotEmpty());

        // Verify Mock
        verify(mockrepo, times(1)).findAll();
    }

    @Test
    public void testAdd() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(11);
        ArgumentCaptor<HouseEntity> arg = ArgumentCaptor.forClass(HouseEntity.class);
        when(mockrepo.save(arg.capture())).thenReturn(createTestHouse(id));

        // Run Tests
        mockMvc.perform(post("/houses")
                .content(asJsonString(new HouseEntity("1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());

        // Verify Mock
        HouseEntity result = arg.getValue();
        assertNotNull(result);
        assertNotNull(result.getAddress());
        assertNotNull(result.getPrice());
        verify(mockrepo, times(1)).save(any());
    }

    @Test
    public void testUpdateNewFails() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(20);
        when(mockrepo.findById(id)).thenThrow(new HouseNotFoundException(id));

        // Run Test
        mockMvc.perform(put("/houses/" + id.toString())
                .content(asJsonString(new HouseEntity(id, "1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());

        // Verify Mocks
        verify(mockrepo, times(1)).findById(id);
        verify(mockrepo, never()).save(any());
    }

    @Test
    public void testUpdateExisting() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(2);
        when(mockrepo.findById(id)).thenReturn(Optional.of(createTestHouse(id)));
        ArgumentCaptor<HouseEntity> arg = ArgumentCaptor.forClass(HouseEntity.class);
        when(mockrepo.save(arg.capture())).thenReturn(createTestHouse(id));

        // Run Test
        mockMvc.perform(put("/houses/" + id.toString())
                .content(asJsonString(new HouseEntity(id, "1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.address").isNotEmpty())
                .andExpect(jsonPath("$.price").isNotEmpty());

        // Verify Mocks
        verify(mockrepo, times(1)).findById(id);
        verify(mockrepo, times(1)).save(any());
    }

    @Test
    // Id in Body is null
    public void testUpdateNullId() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(2);

        // Run Test
        mockMvc.perform(put("/houses/" + id.toString())
                .content(asJsonString(new HouseEntity("1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());

        // Verify Mocks
        verify(mockrepo, never()).findById(id);
        verify(mockrepo, never()).save(any());
    }

    @Test
    // Id in Body doesn't match parameter
    public void testUpdateWrongID() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(2);
        when(mockrepo.findById(id)).thenThrow(new HouseNotFoundException(id));

        // Run Test
        mockMvc.perform(put("/houses/" + id.toString())
                .content(asJsonString(new HouseEntity(Long.valueOf(99), "1234", 987, 3, 2, 1500)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isBadRequest());

        // Verify Mocks
        verify(mockrepo, never()).findById(id);
        verify(mockrepo, never()).save(any());
    }

    @Test
    public void testDeleteValid() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(4);
        when(mockrepo.findById(id)).thenReturn(Optional.of(createTestHouse(id)));

        // Run Test
        mockMvc.perform(delete("/houses/" + id.toString()))
                .andDo(print()).andExpect(status().isOk());

        // Mock Stub
        when(mockrepo.findById(id)).thenThrow(new HouseNotFoundException(id));

        // Should no longer exist
        mockMvc.perform(get("/houses/" + id.toString())).andDo(print())
                .andExpect(status().isNotFound());

        // Verify Mocks
        verify(mockrepo, times(2)).findById(id);
        verify(mockrepo, times(1)).deleteById(id);
    }

    @Test
    // Invalid ID -> 404
    public void testDeleteInvalid() throws Exception {
        // Mock Stubs
        Long id = Long.valueOf(99);
        when(mockrepo.findById(id)).thenThrow(new HouseNotFoundException(id));

        // Run Test
        mockMvc.perform(delete("/houses/" + id.toString()))
                .andDo(print()).andExpect(status().isNotFound());

        // Verify Mocks
        verify(mockrepo, times(1)).findById(id);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HouseEntity createTestHouse(Long id) {
        return new HouseEntity(id, "1234", 1234, 1, 1, 1);
    }

    private Iterable<HouseEntity> createTestHouseList(int size) {
        List<HouseEntity> he = new ArrayList<HouseEntity>();
        for (int i = 0; i < size; i++) {
            he.add(createTestHouse(Long.valueOf(i)));
        }
        return he;
    }

}

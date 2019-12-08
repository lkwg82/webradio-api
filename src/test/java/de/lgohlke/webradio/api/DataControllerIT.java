package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.StationInfo;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJson
public class DataControllerIT {
    @MockBean
    private DataService dataService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void shouldFetchStationInfo() {
        var info = new StationInfo();
        info.setName("test");
        when(dataService.fetchStationInfo(100)).thenReturn(info);

        mockMvc.perform(MockMvcRequestBuilders.get("/stationInfo")
                                              .queryParam("stationId", 100 + ""))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value("test"))
        ;
    }

    @Test
    @SneakyThrows
    void shouldQueryStations() {
        when(dataService.queryForStations("test")).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/queryStations")
                                              .queryParam("query", "test"))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }
}

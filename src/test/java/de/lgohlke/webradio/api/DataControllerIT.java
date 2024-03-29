package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.NowPlaying;
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

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        when(dataService.queryForStations("101")).thenReturn(List.of(info));

        mockMvc.perform(get("/stationInfo").queryParam("stationId", "101"))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.name").value("test"))
        ;
    }

    @Test
    @SneakyThrows
    void shouldFailFetchStationInfo() {
        mockMvc.perform(get("/stationInfo").queryParam("stationId", "100"))
               .andExpect(status().is(404))
        ;
    }

    @Test
    @SneakyThrows
    void shouldQueryStations() {
        when(dataService.queryForStations("test")).thenReturn(List.of());

        mockMvc.perform(get("/queryStations").queryParam("query", "test"))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        ;
    }

    @Test
    @SneakyThrows
    void shouldFetchNowPlaying() {
        var info = new NowPlaying("Mr & Mr - super Song", "cosmo");
        when(dataService.fetchNowPlayingOnStation("100")).thenReturn(info);

        mockMvc.perform(get("/nowPlaying").queryParam("stationId", "100"))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.title").value("Mr & Mr - super Song"))
               .andExpect(jsonPath("$.stationName").value("cosmo"))
        ;
    }
}

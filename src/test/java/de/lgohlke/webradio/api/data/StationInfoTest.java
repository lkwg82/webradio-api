package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class StationInfoTest {
    @Test
    @SneakyThrows
    void mapStationInfo() {
        var file = ResourceUtils.getFile("classpath:station_info.json");

        var mapper = new ObjectMapper();
        var stationInfo = mapper.readValue(file, StationInfo.class);

//        mapper.writerWithDefaultPrettyPrinter()
//              .writeValue(System.out, stationInfo);

        assertThat(stationInfo.getLogo300x300()).isNotBlank();
        assertThat(stationInfo.getStreamUrls()).isNotEmpty();
    }
}

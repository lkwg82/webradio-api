package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.StationInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataServiceTest {
    @Test
    void shouldGetStationInfo() {
        StationInfo stationInfo = new DataService().fetchStationInfo(2459);

        assertThat(stationInfo.getLogo300x300()).isNotBlank();
    }

    @Test
    void shouldFailGetStationInfo() {
        StationInfo stationInfo = new DataService().fetchStationInfo(-1);

        assertThat(stationInfo.getLogo300x300()).isBlank();
    }

}

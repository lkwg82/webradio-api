package de.lgohlke.webradio.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataServiceTest {

    private final DataService dataService = new DataService();

    @Test
    void shouldGetStationInfo() {
        var stationInfo = dataService.fetchStationInfo(2459);

        assertThat(stationInfo.getLogo300x300()).isNotBlank();
    }

    @Test
    void shouldFailGetStationInfo() {
        var stationInfo = dataService.fetchStationInfo(-1);

        assertThat(stationInfo.getLogo300x300()).isBlank();
    }

    @Test
    void shouldGetQueryForCosmos() {
        var result = dataService.queryForStations("COSMOS");

        assertThat(result).isNotEmpty();
    }
}

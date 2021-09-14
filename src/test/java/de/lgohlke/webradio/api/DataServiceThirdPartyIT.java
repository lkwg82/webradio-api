package de.lgohlke.webradio.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataServiceThirdPartyIT {

    private final DataService dataService = new DataService();

    @Test
    void shouldGetStationInfo() {
        var stationInfo = dataService.fetchStationInfo("cosmo");

        assertThat(stationInfo.getLogo300x300()).isNotBlank();
    }

    @Test
    void shouldFailGetStationInfo() {
        var stationInfo = dataService.fetchStationInfo("");

        assertThat(stationInfo.getLogo300x300()).isBlank();
    }

    @Test
    void shouldGetQueryForCosmos() {
        var result = dataService.queryForStations("COSMOS");

        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldGetNowPlaying() {
        var result = dataService.fetchNowPlayingOnStation("cosmo");

        assertThat(result.getStationName()).isNotEmpty();
        assertThat(result.getTitle()).isNotEmpty();
    }

    @Test
    void shouldFailGetNowPlaying_with_empty() {
        var result = dataService.fetchNowPlayingOnStation("");

        assertThat(result.getStationName()).isEqualTo("client error");
    }
}

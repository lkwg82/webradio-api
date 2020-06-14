package de.lgohlke.webradio.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataServiceThirdPartyIT {

    private final DataService dataService = new DataService();
    private final int stationId_for_Cosmos = 2459;
    private final int stationId_invalid = -1;

    @Test
    void shouldGetStationInfo() {
        var stationInfo = dataService.fetchStationInfo(stationId_for_Cosmos);

        assertThat(stationInfo.getLogo300x300()).isNotBlank();
    }

    @Test
    void shouldFailGetStationInfo() {
        var stationInfo = dataService.fetchStationInfo(stationId_invalid);

        assertThat(stationInfo.getLogo300x300()).isBlank();
    }

    @Test
    void shouldGetQueryForCosmos() {
        var result = dataService.queryForStations("COSMOS");

        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldGetNowPlaying() {
        var result = dataService.fetchNowPlayingOnStation(stationId_for_Cosmos);

        assertThat(result.getStationName()).isNotEmpty();
        assertThat(result.getTitle()).isNotEmpty();
    }

    @Test
    void shouldGetNowPlaying_with_empty() {
        var result = dataService.fetchNowPlayingOnStation(stationId_invalid);

        assertThat(result.getStationName()).isEqualTo("<emptyStationname>");
        assertThat(result.getTitle()).isEqualTo("<emptyTitle>");
    }
}

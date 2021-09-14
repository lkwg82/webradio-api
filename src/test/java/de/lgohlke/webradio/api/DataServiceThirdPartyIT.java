package de.lgohlke.webradio.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataServiceThirdPartyIT {

    private final DataService dataService = new DataService();

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

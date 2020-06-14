package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class NowPlaying {
    private final String title;
    @JsonAlias("stationId")
    private final String stationName;

    public NowPlaying() {
        this("<emptyTitle>", "<emptyStationname>");
    }

    public NowPlaying(String title, String stationName) {
        this.title = title;
        this.stationName = stationName;
    }
}

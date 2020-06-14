package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;

@Getter
public class NowPlaying {
    private String title = "<emptyTitle>";
    @JsonAlias("stationId")
    private String stationName = "<emptyStationname>";

    public NowPlaying() {
    }

    public NowPlaying(String title, String stationName) {
        this.title = title;
        this.stationName = stationName;
    }
}

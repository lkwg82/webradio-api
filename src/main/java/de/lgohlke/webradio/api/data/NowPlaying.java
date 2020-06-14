package de.lgohlke.webradio.api.data;

import lombok.Getter;

@Getter
public class NowPlaying {
    private String title = "<emptyTitle>";
    private String stationName = "<emptyStationname>";

    public NowPlaying() {
    }

    public NowPlaying(String title, String stationName) {
        this.title = title;
        this.stationName = stationName;
    }
}

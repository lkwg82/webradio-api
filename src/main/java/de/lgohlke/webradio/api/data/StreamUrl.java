package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.net.URI;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
class StreamUrl {
    private URI streamUrl = URI.create("");
    private int sampleRate = 0;
    private String playingMode = "";
    private String streamContentFormat = "";
    private String streamStatus = "";
}

package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.net.URI;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
class StreamUrl {
    private URI url = URI.create("");
    private String contentFormat = "";
}

package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class StationInfo {
    private String logo300x300 = "";
    private String name = "";
    private List<StreamUrl> streamUrls = List.of();
}

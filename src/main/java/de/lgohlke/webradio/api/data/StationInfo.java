package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationInfo {
    private String logo300x300 = "";
    private String name = "";
    private List<StreamUrl> streamUrls = List.of();
}

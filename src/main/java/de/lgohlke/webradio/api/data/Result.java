package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private int totalCount;

    private List<StationInfo> playables = List.of();

    public List<StationInfo> getMatches2() {
        return playables;
    }
}

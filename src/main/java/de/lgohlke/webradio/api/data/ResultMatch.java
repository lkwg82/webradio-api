package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class ResultMatch {
    private int id = -1;
    private StationInfo station = new StationInfo();
}

package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.StationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DataController {
    private final DataService service;

    @GetMapping(value = "/stationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    StationInfo getStationInfo(@RequestParam("stationId") int stationId) {
        return service.fetchStationInfo(stationId);
    }
}

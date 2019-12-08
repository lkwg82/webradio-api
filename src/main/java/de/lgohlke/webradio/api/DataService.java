package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.StationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Service
@Slf4j
class DataService {
    private final RestTemplate template = new RestTemplateBuilder()
            .rootUri("https://api.radio.de")
            .defaultHeader(HttpHeaders.USER_AGENT, "radio.de 4.14.0 (OnePlus/ONEPLUS A3003; Android 9; de_DE)")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
            .build();

    StationInfo fetchStationInfo(int stationId) {

        var uri = UriComponentsBuilder.fromPath("/info/v2/search/station")
                                      .queryParam("apikey", "7bce309a37dc2527104e5ab7b0641c9f20a76aae")
                                      .queryParam("station", stationId)
                                      .queryParam("streamcontentformats", "mp3,aac")
                                      .queryParam("enrich", "true")
                                      .queryParam("locale", "de_DE")
                                      .build()
                                      .toString();
        try {
            var responseEntity = template.getForEntity(uri, StationInfo.class);
            return responseEntity
                    .getBody();
        } catch (InvalidMediaTypeException | HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return new StationInfo();
        }
    }
}

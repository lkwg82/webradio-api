package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.NowPlaying;
import de.lgohlke.webradio.api.data.Result;
import de.lgohlke.webradio.api.data.ResultMatch;
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
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
class DataService {
    private static final String API_KEEEEY = "7bce309a37dc2527104e5ab7b0641c9f20a76aae";

    private static RestTemplate buildWithRootUri(String uri) {
        return new RestTemplateBuilder()
                .rootUri(uri)
                .defaultHeader(HttpHeaders.USER_AGENT, "radio.de 4.14.0 (OnePlus/ONEPLUS A3003; Android 9; de_DE)")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
                .build();
    }

    private final RestTemplate template = buildWithRootUri("https://api.radio.de");
    private final RestTemplate prodApi = buildWithRootUri("https://prod.radio-api.net");

    StationInfo fetchStationInfo(int stationId) {

        var uri = UriComponentsBuilder.fromPath("/info/v2/search/station")
                .queryParam("apikey", API_KEEEEY)
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

    List<ResultMatch> queryForStations(String query) {
        var uri = UriComponentsBuilder.fromPath("/info/v2/search/stationsonly")
                .queryParam("apikey", API_KEEEEY)
                .queryParam("query", query)
                .queryParam("streamcontentformats", "mp3,aac")
                .queryParam("enrich", "true")
                .queryParam("locale", "de_DE")
                .queryParam("pageindex", 1)
                .queryParam("sizeperpage", "20")
                .build()
                .toString();
        try {
            var responseEntity = template.getForEntity(uri, Result.class);
            return Optional.ofNullable(responseEntity.getBody())
                    .map(Result::getMatches)
                    .orElse(List.of());
        } catch (InvalidMediaTypeException | HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return List.of();
        }
    }

    public NowPlaying fetchNowPlayingOnStation(int stationId) {
        var url = "/stations/now-playing?stationIds=" + stationId;
        try {
            var responseEntity = prodApi.getForEntity(url, NowPlaying[].class);

            var nowPlayings = responseEntity.getBody();
            if (null == nowPlayings) {
                return new NowPlaying("<null>", "<null>");
            }
            if (nowPlayings.length == 0) {
                return new NowPlaying();
            }
            return nowPlayings[0];
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return new NowPlaying(e.getMessage(), "client error");
        }
    }

}

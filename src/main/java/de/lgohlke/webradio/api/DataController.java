package de.lgohlke.webradio.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.lgohlke.webradio.api.data.NowPlaying;
import de.lgohlke.webradio.api.data.StationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
class DataController {
    private final DataService service;
    private CacheLoader<String, List<StationInfo>> stationsLoader = new CacheLoader<>() {
        @Override
        public List<StationInfo> load(String key) {
            log.info("refreshing for {}", key);
            return service.queryForStations(key);
        }
    };
    private final LoadingCache<String, List<StationInfo>> stations = CacheBuilder.newBuilder()
                                                                                 .maximumSize(100)
                                                                                 .refreshAfterWrite(Duration.ofHours(5))
                                                                                 .build(stationsLoader);

    private final CacheLoader<String, NowPlaying> nowPlayingCachLoader = new CacheLoader<>() {
        @Override
        public NowPlaying load(String key) {
            log.info("refreshing NowPlaying for stationId '{}'", key);
            return service.fetchNowPlayingOnStation(key);
        }
    };

    private final LoadingCache<String, NowPlaying> nowPlayings = CacheBuilder.newBuilder()
                                                                             .maximumSize(100)
                                                                             .expireAfterWrite(Duration.ofSeconds(5))
                                                                             .build(nowPlayingCachLoader);

    @GetMapping(value = "/stationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    StationInfo getStationInfo(@RequestParam("stationId") String stationId) throws ExecutionException {
        var stationInfos = stations.get(stationId);
        if (stationInfos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "station not found");
        }
        return stationInfos.get(0);
    }

    @GetMapping(value = "/queryStations", produces = MediaType.APPLICATION_JSON_VALUE)
    List<StationInfo> queryStations(@RequestParam("query") String query) throws ExecutionException {
        return stations.get(query);
    }

    @GetMapping(value = "/nowPlaying", produces = MediaType.APPLICATION_JSON_VALUE)
    NowPlaying getNowPlayings(@RequestParam("stationId") String stationId) throws ExecutionException {
        return nowPlayings.get(stationId);
    }

    @RequestMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        // language=HTML
        return """
                <ul>
                <li><a href="/nowPlaying?stationId=cosmo">/nowPlaying?stationId=cosmo</a></li>
                <li><a href="/queryStations?query=cosmo">/queryStations?query=cosmo</a></li>
                <li><a href="/stationInfo?stationId=cosmo">/stationInfo?stationId=cosmo</a></li>
                   </ul>             
                """;
    }
}

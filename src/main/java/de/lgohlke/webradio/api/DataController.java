package de.lgohlke.webradio.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.lgohlke.webradio.api.data.NowPlaying;
import de.lgohlke.webradio.api.data.StationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
class DataController {
    private final DataService service;

    private final LoadingCache<String, StationInfo> stationInfos = CacheBuilder.newBuilder()
                                                                               .maximumSize(100)
                                                                               .refreshAfterWrite(Duration.ofHours(5))
                                                                               .build(new CacheLoader<>() {
                                                                                   @Override
                                                                                   public StationInfo load(String key) {
                                                                                       log.info(
                                                                                               "refreshing StationInfo for '{}'",
                                                                                               key);
                                                                                       return service.fetchStationInfo(
                                                                                               key);
                                                                                   }
                                                                               });

    private final LoadingCache<String, List<StationInfo>> cache2 = CacheBuilder.newBuilder()
                                                                               .maximumSize(100)
                                                                               .refreshAfterWrite(Duration.ofHours(5))
                                                                               .build(new CacheLoader<>() {
                                                                                   @Override
                                                                                   public List<StationInfo> load(String key) {
                                                                                       log.info("refreshing for {}",
                                                                                                key);
                                                                                       return service.queryForStations(
                                                                                               key);
                                                                                   }
                                                                               });

    private final LoadingCache<String, NowPlaying> nowPlayings = CacheBuilder.newBuilder()
                                                                             .maximumSize(100)
                                                                             .expireAfterWrite(Duration.ofSeconds(5))
                                                                             .build(new CacheLoader<>() {
                                                                                 @Override
                                                                                 public NowPlaying load(String key) {
                                                                                     log.info(
                                                                                             "refreshing NowPlaying for stationId '{}'",
                                                                                             key);
                                                                                     return service.fetchNowPlayingOnStation(
                                                                                             key);
                                                                                 }
                                                                             });

    @GetMapping(value = "/stationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    StationInfo getStationInfo(@RequestParam("stationId") String stationId) throws ExecutionException {
        return stationInfos.get(stationId);
    }

    @GetMapping(value = "/queryStations", produces = MediaType.APPLICATION_JSON_VALUE)
    List<StationInfo> queryStations(@RequestParam("query") String query) throws ExecutionException {
        return cache2.get(query);
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

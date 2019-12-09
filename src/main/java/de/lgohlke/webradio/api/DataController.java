package de.lgohlke.webradio.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.lgohlke.webradio.api.data.ResultMatch;
import de.lgohlke.webradio.api.data.StationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final LoadingCache<Integer, StationInfo> cache = CacheBuilder.newBuilder()
                                                                         .maximumSize(100)
                                                                         .refreshAfterWrite(Duration.ofHours(5))
                                                                         .build(new CacheLoader<>() {
                                                                             @Override
                                                                             public StationInfo load(Integer key) {
                                                                                 log.info("refreshing for {}", key);
                                                                                 return service.fetchStationInfo(key);
                                                                             }
                                                                         });
    private final LoadingCache<String, List<ResultMatch>> cache2 = CacheBuilder.newBuilder()
                                                                               .maximumSize(100)
                                                                               .refreshAfterWrite(Duration.ofHours(5))
                                                                               .build(new CacheLoader<>() {
                                                                                   @Override
                                                                                   public List<ResultMatch> load(String key) {
                                                                                       log.info("refreshing for {}",
                                                                                                key);
                                                                                       return service.queryForStations(
                                                                                               key);
                                                                                   }
                                                                               });

    @GetMapping(value = "/stationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    StationInfo getStationInfo(@RequestParam("stationId") int stationId) throws ExecutionException {
        return cache.get(stationId);
    }

    @GetMapping(value = "/queryStations", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ResultMatch> queryStations(@RequestParam("query") String query) throws ExecutionException {
        return cache2.get(query);
    }
}

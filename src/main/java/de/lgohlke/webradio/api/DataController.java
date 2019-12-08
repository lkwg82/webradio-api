package de.lgohlke.webradio.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.lgohlke.webradio.api.data.StationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@Slf4j
class DataController {
    private final DataService service;

    private final LoadingCache<Integer, StationInfo> cache = CacheBuilder.newBuilder()
                                                                         .refreshAfterWrite(Duration.ofHours(5))
                                                                         .build(new CacheLoader<>() {
                                                                             @Override
                                                                             public StationInfo load(Integer key) {
                                                                                 log.info("refreshing for {}", key);
                                                                                 return service.fetchStationInfo(key);
                                                                             }
                                                                         });

    @GetMapping(value = "/stationInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    StationInfo getStationInfo(@RequestParam("stationId") int stationId) throws ExecutionException {
        return cache.get(stationId);
    }
}
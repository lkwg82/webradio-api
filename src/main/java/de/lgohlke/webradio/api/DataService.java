package de.lgohlke.webradio.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.lgohlke.webradio.api.data.NowPlaying;
import de.lgohlke.webradio.api.data.Result;
import de.lgohlke.webradio.api.data.StationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
class DataService {
    private static RestTemplate buildWithRootUri(String uri) {
        return new RestTemplateBuilder()
                .rootUri(uri)
                .defaultHeader(HttpHeaders.USER_AGENT, "radio.de")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .additionalMessageConverters(new HttpMessageConverter() {
                    @Override
                    public List<MediaType> getSupportedMediaTypes() {
                        return List.of(MediaType.TEXT_PLAIN);
                    }

                    @Override
                    public Object read(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                        var bytes = inputMessage.getBody()
                                                .readAllBytes();
                        var objectMapper = new ObjectMapper();
                        return objectMapper.readValue(bytes, clazz);
                    }

                    @Override
                    public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

                    }

                    @Override
                    public boolean canWrite(Class clazz, MediaType mediaType) {
                        return false;
                    }

                    @Override
                    public boolean canRead(Class clazz, MediaType mediaType) {
                        return true;
                    }
                })
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
                .build();
    }

    private final RestTemplate template = buildWithRootUri("https://prod.radio-api.net");

    List<StationInfo> queryForStations(String query) {
        var uri = UriComponentsBuilder.fromPath("/stations/search")
                                      .queryParam("count", 10)
                                      .queryParam("offset", 0)
                                      .queryParam("query", query)
                                      .build()
                                      .toString();
        try {
            var responseEntity = template.getForEntity(uri, Result.class);
            return Optional.ofNullable(responseEntity.getBody())
                           .map(Result::getMatches2)
                           .orElse(List.of());
        } catch (InvalidMediaTypeException | HttpClientErrorException e) {
            log.error(e.getMessage(), e);
            return List.of();
        }
    }

    public NowPlaying fetchNowPlayingOnStation(String stationId) {
        var url = "/stations/now-playing?stationIds=" + stationId;
        try {
            var responseEntity = template.getForEntity(url, NowPlaying[].class);

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

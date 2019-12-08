package de.lgohlke.webradio.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class RadioDEClientTest {
    private final RestTemplate template = new RestTemplateBuilder()
            .rootUri("https://api.radio.de")
            .defaultHeader(HttpHeaders.USER_AGENT, "radio.de 4.14.0 (OnePlus/ONEPLUS A3003; Android 9; de_DE)")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
            .build();

    @Test
    void simpleCall() {

        var uri = UriComponentsBuilder.fromPath("/info/v2/search/station")
                                      .queryParam("apikey", "7bce309a37dc2527104e5ab7b0641c9f20a76aae")
                                      .queryParam("station", "2459")
                                      .queryParam("streamcontentformats", "mp3,aac")
                                      .queryParam("enrich", "true")
                                      .queryParam("locale", "de_DE")
                                      .build()
                                      .toString();

        var response = template.getForObject(uri, String.class);

        assertThat(response).hasSizeGreaterThan(500);
    }
}

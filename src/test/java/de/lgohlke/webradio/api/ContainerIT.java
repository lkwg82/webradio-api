package de.lgohlke.webradio.api;

import de.lgohlke.webradio.api.data.StationInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class ContainerIT {
    @Container
    public GenericContainer api = new GenericContainer<>("lkwg82/webradio-api")
            .withExposedPorts(8080);

    @Test
    void test() {
        var ip = api.getHost();
        var port = api.getFirstMappedPort();
        String rootUri = "http://" + ip + ":" + port;
        var template = new RestTemplateBuilder().rootUri(rootUri).build();

        var responseEntity = template.getForEntity("/stationInfo?stationId=2459", StationInfo.class);
        var stationInfo = responseEntity.getBody();

        assertThat(stationInfo).isNotNull();
        assertThat(stationInfo.getName()).isEqualTo("COSMO");
    }
}

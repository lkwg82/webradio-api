package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTest {
    @Test
    @SneakyThrows
    void map() {
        var file = ResourceUtils.getFile("classpath:query_result.json");

        var mapper = new ObjectMapper();
        var result = mapper.readValue(file, Result.class);

//        mapper.writerWithDefaultPrettyPrinter()
//              .writeValue(System.out, result);
        assertThat(result.getTotalCount()).isEqualTo(30);
        assertThat(result.getPlayables()).hasSize(10);

        var stationInfo = result.getMatches2()
                                .get(0);
        assertThat(stationInfo.getId()).isEqualTo("cosmo");
        assertThat(stationInfo.getStreams()).hasSize(4);
    }
}

package de.lgohlke.webradio.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private List<Categories> categories = List.of();

    List<ResultMatch> getMatches() {
        return categories.stream()
                         .map(Categories::getMatches)
                         .flatMap(List::stream)
                         .collect(Collectors.toUnmodifiableList());
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Categories {
        private List<ResultMatch> matches = List.of();
    }
}

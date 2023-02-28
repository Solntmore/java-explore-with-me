package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;

    private final String mainUrl;

    public List<Long> getStats(String start, String end, Collection<String> uris, boolean unique) {
        String statsResourceUrl = mainUrl + "/stats";
        String parameters = "?start=" + start +
                "&end=" + end +
                "&uris=" + uris.toString() +
                "&unique=" + unique;

        ResponseEntity<List<ViewStats>> responseEntity = restTemplate.exchange(statsResourceUrl + parameters,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });

        List<ViewStats> stats = responseEntity.getBody();

        if (stats == null || stats.isEmpty()) {
            return new ArrayList<>();
        } else {
            return stats.stream()
                    .map(ViewStats::getHits)
                    .collect(Collectors.toList());
        }
    }

    public void saveRequest(RequestHitDto requestHitDto) {
        String statsResourceUrl = mainUrl + "/hit";
        HttpEntity<RequestHitDto> request = new HttpEntity<>(requestHitDto);
        restTemplate.postForEntity(statsResourceUrl, request, RequestHitDto.class);
    }
}

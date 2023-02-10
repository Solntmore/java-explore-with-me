package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;

    private final String mainUrl;

/*
{{baseUrl}}/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}&unique=false
 */

    public ArrayList<ViewStats> getStats(String start, String end, Collection<String> uris, boolean unique) {
        String statsResourceUrl = mainUrl + "/stats";
        String parameters = "?start=" + start +
                "&end=" + end +
                "&uris=" + uris.toString() +
                "&unique=" + unique;
        List<?> list = restTemplate.getForObject(statsResourceUrl + parameters, ArrayList.class,
                ViewStats.class);
        return (ArrayList<ViewStats>) list;
    }

    public void saveRequest(RequestHitDto requestHitDto) {
        String statsResourceUrl = mainUrl + "/hit";
        HttpEntity<RequestHitDto> request = new HttpEntity<>(requestHitDto);
         /*restTemplate.postForObject(statsResourceUrl, request, RequestHitDto.class);*/
         restTemplate.postForEntity(statsResourceUrl, request, RequestHitDto.class);
    }
}

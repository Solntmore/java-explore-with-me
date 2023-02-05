package ru.practicum;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;
/*
{{baseUrl}}/stats?start=2020-05-05 00:00:00&end=2035-05-05 00:00:00&uris={{uri}}&unique=false
 */

    public ArrayList<ViewStats> getStats(String start, String end, Collection<String> uris, boolean unique) {
        String statsResourceUrl = "http://localhost:9090/stats";
        String parameters = "?start=" + start +
                "&end=" + end +
                "&uris=" + uris.toString() +
                "&unique=" + unique;
        ArrayList<ViewStats> list = restTemplate.getForObject(statsResourceUrl + parameters, ArrayList.class, ViewStats.class);
        return list;
    }
}

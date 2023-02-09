package ru.practicum.ewmserv.event.exceptions.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.StatsClient;

@Configuration
public class StatsClientConfig {

    @Bean
    StatsClient client() {
        RestTemplate restTemplate = new RestTemplate();
        return new StatsClient(restTemplate);
    }
}

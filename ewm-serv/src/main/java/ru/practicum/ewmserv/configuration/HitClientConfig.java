package ru.practicum.ewmserv.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.HitClient;

@Configuration
public class HitClientConfig {

    @Bean
    HitClient client() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        return new HitClient("http://localhost:9090", restTemplateBuilder);
    }
}

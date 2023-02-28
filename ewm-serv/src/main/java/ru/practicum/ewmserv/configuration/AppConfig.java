package ru.practicum.ewmserv.configuration;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.StatsClient;

import java.time.format.DateTimeFormatter;

@Configuration
public class AppConfig {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Value("${stats-service.url}")
    private String serverUrl;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.simpleDateFormat(DATE_TIME_FORMAT);
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
            jacksonObjectMapperBuilder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        };


    }

    @Bean
    StatsClient client() {
        RestTemplate restTemplate = new RestTemplate();

        return new StatsClient(restTemplate, serverUrl);
    }
}

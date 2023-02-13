package ru.practicum;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Data
@Jacksonized
@Builder
public class ResponseHitDto {

    private final Long id;

    private final String app;

    private final String uri;

    private final String ip;

    private final LocalDateTime created;
}

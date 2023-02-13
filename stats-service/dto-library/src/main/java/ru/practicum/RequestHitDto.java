package ru.practicum;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Jacksonized
@Builder
public class RequestHitDto implements Serializable {
    private final String app;
    private final String uri;
    private final String ip;
    private String timestamp;
}
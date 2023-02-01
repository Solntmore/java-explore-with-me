package ru.practicum;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestHitDto implements Serializable {
    private final String app;
    private final String uri;
    private final String ip;
    private String timestamp;
}
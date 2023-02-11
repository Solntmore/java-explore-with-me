package ru.practicum.ewmserv.request.dto;

import lombok.Data;
import ru.practicum.ewmserv.enums.RequestStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.request.model.Request} entity
 */
@Data
public class ResponseRequestDto implements Serializable {

    private final Long id;

    private final LocalDateTime created;

    private final Long event;

    private final Long requester;

    private final RequestStatus status;
}
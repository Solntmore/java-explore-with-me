package ru.practicum.ewmserv.event.dto;

import lombok.Data;
import ru.practicum.ewmserv.enums.UserState;
import ru.practicum.ewmserv.event.model.Location;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.event.model.Event} entity
 */
@Data
public class EventPatchDto implements Serializable {

    private final String annotation;

    private final Long categoryId;

    private final String description;

    private final LocalDateTime eventDate;

    private final Location location;

    private final Boolean paid;

    private Integer participantLimit;

    private final Boolean requestModeration;

    private final UserState stateAction;

    private final String title;
}
package ru.practicum.ewmserv.event.dto;

import lombok.Data;
import ru.practicum.ewmserv.event.model.Location;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.event.model.Event} entity
 */
@Data
public class NewEventDto implements Serializable {

    @Size(min = 20, max = 2000)
    private final String annotation;

    private final int category;

    @Size(min = 20, max = 7000)
    private final String description;

    private final LocalDateTime eventDate;

    private final Location location;

    private final boolean paid;

    private final int participantLimit;

    private final boolean requestModeration;

    @Size(min = 3, max = 120)
    private final String title;
}
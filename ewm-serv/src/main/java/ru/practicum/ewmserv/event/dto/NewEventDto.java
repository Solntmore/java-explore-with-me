package ru.practicum.ewmserv.event.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewmserv.event.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.event.model.Event} entity
 */
@Data
@Builder
@Jacksonized
public class NewEventDto implements Serializable {

    @Size(min = 20, max = 2000)
    @NotNull
    private final String annotation;

    private final Long category;

    @Size(min = 20, max = 7000)
    @NotNull
    private final String description;

    private final LocalDateTime eventDate;

    private final Location location;

    private final Boolean paid;

    private final int participantLimit;

    private final Boolean requestModeration;
    
    @Size(min = 3, max = 120)
    @NotNull
    private final String title;
}
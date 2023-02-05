package ru.practicum.ewmserv.event.dto;

import lombok.Data;
import ru.practicum.ewmserv.enums.AdminState;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.model.Location;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.event.model.Event} entity
 */
@Data
public class UpdateEventAdminDto implements Serializable {

    private final String annotation;

    private final Long categoryId;

    private final String description;

    private final LocalDateTime eventDate;

    private final Location location;

    private final boolean paid;

    private final int participantLimit;

    private final boolean requestModeration;

    private final AdminState stateAction;

    private final String title;

}
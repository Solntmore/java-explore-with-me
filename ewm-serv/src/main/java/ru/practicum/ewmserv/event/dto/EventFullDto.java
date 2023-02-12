package ru.practicum.ewmserv.event.dto;

import lombok.Data;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.comment.dto.ResponseCommentDto;
import ru.practicum.ewmserv.event.model.Location;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * A DTO for the {@link ru.practicum.ewmserv.event.model.Event} entity
 */
@Data
public class EventFullDto implements Serializable {

    private final Long id;

    @Size(min = 20, max = 2000)
    private final String annotation;

    private final ResponseCategoryDto category;

    private int confirmedRequests;

    private final LocalDateTime createdOn;

    @Size(min = 20, max = 7000)
    private final String description;

    private final LocalDateTime eventDate;

    private final ResponseUserDto initiator;

    private final Location location;

    private final boolean paid;

    private final int participantLimit;

    private final LocalDateTime publishedOn;

    private final boolean requestModeration;

    private final StateAction state;

    @Size(min = 3, max = 120)
    private final String title;

    private Long views;

    private Set<ResponseCommentDto> comments;


}
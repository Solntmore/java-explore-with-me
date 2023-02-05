package ru.practicum.ewmserv.event.dto;

import lombok.Data;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.event.model.Event} entity
 */
@Data
public class EventShortDto implements Serializable {

    private final Long id;

    @Size(min = 20, max = 2000)
    private final String annotation;

    private LocalDateTime createdOn;

    private final ResponseCategoryDto category;

    private int confirmedRequests;

    private final LocalDateTime eventDate;

    private final ResponseUserDto initiator;

    private final boolean paid;

    @Size(min = 3, max = 120)
    private final String title;
    
    private Long views;
}
package ru.practicum.ewmserv.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewmserv.event.dto.EventShortDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A DTO for the {@link ru.practicum.ewmserv.compilation.model.Compilation} entity
 */
@Data
@AllArgsConstructor
@Builder
public class ResponseCompilationDto implements Serializable {
    private final Long id;
    private final boolean pinned;
    private final String title;
    private ArrayList<EventShortDto> events;
}
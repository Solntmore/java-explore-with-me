package ru.practicum.ewmserv.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmserv.event.dto.EventShortDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A DTO for the {@link ru.practicum.ewmserv.compilation.model.Compilation} entity
 */
@Data
@AllArgsConstructor
public class ResponseCompilationDto implements Serializable {
    private final Long id;
    private ArrayList<EventShortDto> eventList;
    private final boolean pinned;
    private final String title;
}
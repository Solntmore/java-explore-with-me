package ru.practicum.ewmserv.compilation.dto;

import lombok.Data;
import ru.practicum.ewmserv.event.dto.EventShortDto;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A DTO for the {@link ru.practicum.ewmserv.compilation.model.Compilations} entity
 */
@Data
public class ResponseCompilationsDto implements Serializable {

    private final Long id;

    private final ArrayList<EventShortDto> events;

    private final boolean pinned;

    private final String title;
}
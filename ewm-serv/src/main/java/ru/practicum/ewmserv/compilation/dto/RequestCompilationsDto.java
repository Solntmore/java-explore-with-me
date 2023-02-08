package ru.practicum.ewmserv.compilation.dto;

import lombok.Data;
import ru.practicum.ewmserv.event.model.Event;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A DTO for the {@link ru.practicum.ewmserv.compilation.model.Compilations} entity
 */
@Data
public class RequestCompilationsDto implements Serializable {

    private ArrayList<Long> events;

    private final boolean pinned;

    private final String title;
}
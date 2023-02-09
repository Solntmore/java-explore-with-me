package ru.practicum.ewmserv.compilation.dto;

import lombok.Data;
import ru.practicum.ewmserv.compilation.model.Compilation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A DTO for the {@link Compilation} entity
 */
@Data
public class RequestCompilationDto implements Serializable {

    private ArrayList<Long> events;

    private final boolean pinned;

    private final String title;
}
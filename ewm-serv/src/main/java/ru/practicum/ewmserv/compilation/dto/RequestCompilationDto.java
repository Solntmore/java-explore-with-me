package ru.practicum.ewmserv.compilation.dto;

import lombok.Data;
import ru.practicum.ewmserv.compilation.model.Compilation;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A DTO for the {@link Compilation} entity
 */
@Data
public class RequestCompilationDto implements Serializable {

    private final Boolean pinned;
    @NotEmpty
    private final String title;
    private ArrayList<Long> events;
}
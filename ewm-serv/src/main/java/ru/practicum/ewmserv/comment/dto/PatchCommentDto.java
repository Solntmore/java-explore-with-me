package ru.practicum.ewmserv.comment.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewmserv.comment.model.Comment} entity
 */
@Data
public class PatchCommentDto implements Serializable {

    @Size(min = 1, max = 2000)
    @NotNull
    private final String text;
}
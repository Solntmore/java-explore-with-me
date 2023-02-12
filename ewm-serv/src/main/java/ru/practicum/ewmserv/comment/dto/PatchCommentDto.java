package ru.practicum.ewmserv.comment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewmserv.comment.model.Comment} entity
 */
@Data
@Builder
@Jacksonized
public class PatchCommentDto implements Serializable {

    @Size(min = 1, max = 2000)
    @NotBlank
    private final String text;
}
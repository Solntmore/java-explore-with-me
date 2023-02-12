package ru.practicum.ewmserv.comment.dto;

import lombok.Data;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link ru.practicum.ewmserv.comment.model.Comment} entity
 */
@Data
public class ResponseCommentDto implements Serializable {
    private final Long id;
    @Size(min = 1, max = 2000)
    private final String text;
    private final ResponseUserDto author;
    private final LocalDateTime created;
}
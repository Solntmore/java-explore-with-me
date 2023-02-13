package ru.practicum.ewmserv.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewmserv.user.model.User} entity
 */
@Data
public class ResponseUserDto implements Serializable {

    private final Long id;

    private final String email;

    private final String name;
}
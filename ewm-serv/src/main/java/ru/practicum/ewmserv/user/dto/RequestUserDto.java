package ru.practicum.ewmserv.user.dto;

import lombok.Data;
import ru.practicum.ewmserv.user.model.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link User} entity
 */
@Data
public class RequestUserDto implements Serializable {

    @Email(message = "Enter the correct email.")
    @NotNull(message = "Email can`t be null.")
    private final String email;

    @NotBlank(message = "Name may not be blank")
    private final String name;
}
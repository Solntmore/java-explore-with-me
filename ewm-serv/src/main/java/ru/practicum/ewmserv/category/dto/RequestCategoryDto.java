package ru.practicum.ewmserv.category.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewmserv.category.model.Category} entity
 */
@Data
@Builder
@Jacksonized
public class RequestCategoryDto implements Serializable {

    @NotBlank
    private String name;

}
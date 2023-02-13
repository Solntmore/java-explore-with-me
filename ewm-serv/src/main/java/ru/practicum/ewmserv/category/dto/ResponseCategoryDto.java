package ru.practicum.ewmserv.category.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.practicum.ewmserv.category.model.Category} entity
 */
@Data
public class ResponseCategoryDto implements Serializable {

    private final Long id;

    private final String name;
}
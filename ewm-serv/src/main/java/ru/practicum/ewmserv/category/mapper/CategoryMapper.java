package ru.practicum.ewmserv.category.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.category.dto.RequestCategoryDto;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.category.model.Category;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(RequestCategoryDto requestCategoryDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(RequestCategoryDto requestCategoryDto, @MappingTarget Category category);

    ResponseCategoryDto toDto(Category category);


}
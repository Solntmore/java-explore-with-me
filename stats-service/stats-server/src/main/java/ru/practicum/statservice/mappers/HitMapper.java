package ru.practicum.statservice.mappers;

import org.mapstruct.*;
import ru.practicum.IViewStats;
import ru.practicum.RequestHitDto;
import ru.practicum.ResponseHitDto;
import ru.practicum.ViewStats;
import ru.practicum.statservice.model.Hit;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface HitMapper {
    Hit toHitEntity(RequestHitDto requestHitDto);

    ResponseHitDto toDto(Hit hit);

    ViewStats toViewStatsEntity(IViewStats iViewStats);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Hit partialUpdate(RequestHitDto requestHitDto, @MappingTarget Hit hit);
}
package ru.practicum.ewmserv.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.event.model.Location;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface LocationMapper {
    Location toEntity(Location location);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Location partialUpdate(Location location, @MappingTarget Location loc);
}
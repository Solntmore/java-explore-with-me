package ru.practicum.ewmserv.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.event.dto.NewEventDto;
import ru.practicum.ewmserv.event.model.Event;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {LocationMapper.class})
public interface EventMapper {

    Event toEntity(NewEventDto newEventDto);

    NewEventDto toDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdate(NewEventDto newEventDto, @MappingTarget Event event);
}
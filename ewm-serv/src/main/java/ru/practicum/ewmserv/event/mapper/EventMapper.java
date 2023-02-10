package ru.practicum.ewmserv.event.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.category.mapper.CategoryMapper;
import ru.practicum.ewmserv.event.dto.EventFullDto;
import ru.practicum.ewmserv.event.dto.EventShortDto;
import ru.practicum.ewmserv.event.dto.NewEventDto;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.user.mapper.UserMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {LocationMapper.class, CategoryMapper.class, UserMapper.class})
public interface EventMapper {

    @Mapping(target = "category", ignore = true)
    Event toEntityFromNewEventDto(NewEventDto newEventDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "category", target = "category.id")
    Event partialUpdate(NewEventDto newEventDto, @MappingTarget Event event);

    @Mapping(source = "stateAction", target = "state")
    EventFullDto toEventFullDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "state", target = "stateAction")
    Event partialUpdateFromEventFullDto(EventFullDto eventFullDto, @MappingTarget Event event);

    Event toEntityFromEventShortDto(EventShortDto eventShortDto);

    EventShortDto toEventShortDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event partialUpdateEvent(EventShortDto eventShortDto, @MappingTarget Event event);
}
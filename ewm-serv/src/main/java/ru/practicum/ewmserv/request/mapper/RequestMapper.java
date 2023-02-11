package ru.practicum.ewmserv.request.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.request.dto.ResponseRequestDto;
import ru.practicum.ewmserv.request.model.Request;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "requester", target = "requester.id")
    @Mapping(source = "event", target = "event.id")
    Request toEntity(ResponseRequestDto responseRequestDto);

    @InheritInverseConfiguration(name = "toEntity")
    ResponseRequestDto toDto(Request request);

    @InheritConfiguration(name = "toEntity")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Request partialUpdate(ResponseRequestDto responseRequestDto, @MappingTarget Request request);
}
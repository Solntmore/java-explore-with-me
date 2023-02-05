package ru.practicum.ewmserv.user.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.user.dto.RequestUserDto;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;
import ru.practicum.ewmserv.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toEntity(RequestUserDto requestUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(RequestUserDto requestUserDto, @MappingTarget User user);

    ResponseUserDto toDto(User user);

    User toEntity1(ResponseUserDto responseUserDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate1(ResponseUserDto responseUserDto, @MappingTarget User user);
}
package ru.practicum.ewmserv.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.compilation.dto.RequestCompilationsDto;
import ru.practicum.ewmserv.compilation.dto.ResponseCompilationsDto;
import ru.practicum.ewmserv.compilation.model.Compilations;
import ru.practicum.ewmserv.event.mapper.EventMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {EventMapper.class})
public interface CompilationsMapper {

    RequestCompilationsDto toDto(Compilations compilations);

    Compilations toEntity(ResponseCompilationsDto responseCompilationsDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Compilations partialUpdate(ResponseCompilationsDto responseCompilationsDto, @MappingTarget Compilations compilations);
}
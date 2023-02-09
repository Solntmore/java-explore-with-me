package ru.practicum.ewmserv.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.ewmserv.compilation.dto.RequestCompilationDto;
import ru.practicum.ewmserv.compilation.dto.ResponseCompilationDto;
import ru.practicum.ewmserv.compilation.model.Compilation;
import ru.practicum.ewmserv.event.mapper.EventMapper;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {EventMapper.class})
public interface CompilationsMapper {


    Compilation toEntity(RequestCompilationDto requestCompilationDto);

    ResponseCompilationDto toDto(Compilation compilation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Compilation partialUpdate(ResponseCompilationDto responseCompilationDto, @MappingTarget Compilation compilation);
}
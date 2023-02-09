package ru.practicum.ewmserv.compilation.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmserv.compilation.dto.ResponseCompilationDto;
import ru.practicum.ewmserv.compilation.model.Compilation;
import ru.practicum.ewmserv.event.dto.EventShortDto;
import ru.practicum.ewmserv.event.mapper.EventMapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CustomCompilationMapper {

    private final EventMapper eventMapper;

    public ResponseCompilationDto toDto(Compilation compilation) {
        Long id = compilation.getId();
        boolean pinned = compilation.isPinned();
        String title = compilation.getTitle();
        ArrayList<EventShortDto> eventList = (ArrayList<EventShortDto>) compilation.getEventList().stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        return new ResponseCompilationDto(id, eventList, pinned, title);


    }
}

package ru.practicum.ewmserv.compilation.service;

import com.querydsl.core.BooleanBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.ewmserv.compilation.dto.RequestCompilationDto;
import ru.practicum.ewmserv.compilation.dto.ResponseCompilationDto;
import ru.practicum.ewmserv.compilation.exceptions.CompilationNotFoundException;
import ru.practicum.ewmserv.compilation.mapper.CompilationsMapper;
import ru.practicum.ewmserv.compilation.mapper.CustomCompilationMapper;
import ru.practicum.ewmserv.compilation.model.Compilation;
import ru.practicum.ewmserv.compilation.repository.CompilationRepository;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.event.dto.EventShortDto;
import ru.practicum.ewmserv.event.mapper.EventMapper;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.event.model.QEvent;
import ru.practicum.ewmserv.event.repository.EventRepository;
import ru.practicum.ewmserv.event.util.EventFilterForCompilations;
import ru.practicum.ewmserv.request.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewmserv.constants.Constants.DATE_TIME_FORMATTER;

@RequiredArgsConstructor
@Service
public class CompilationsService {

    private final CompilationRepository compilationRepository;
    private final CompilationsMapper compilationsMapper;
    private final CustomCompilationMapper customCompilationMapper;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final EventMapper eventMapper;

    public ResponseCompilationDto postCompilation(RequestCompilationDto requestCompilationDto) {

        Compilation compilation = setEvents(
                compilationsMapper.toEntity(requestCompilationDto), requestCompilationDto);
        ResponseCompilationDto responseCompilationDto = compilationsMapper.toDto(
                compilationRepository.save(compilation));

        return mapEventList(compilation, responseCompilationDto);
    }

    public ResponseCompilationDto patchCompilation(long compId, RequestCompilationDto requestCompilationDto) {
        Compilation oldCompilation = compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException("Compilation with id " + compId + " was not found"));

        Compilation newCompilation = setEvents(
                compilationsMapper.toEntity(requestCompilationDto), requestCompilationDto);

        ResponseCompilationDto responseCompilationDto = compilationsMapper.toDto(
                compilationRepository.patchCompilationByAdmin(oldCompilation, newCompilation));

        return mapEventList(newCompilation, responseCompilationDto);
    }

    public void deleteCompilation(long compId) {
        compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException("Compilation with id " + compId + " was not found"));

        compilationRepository.deleteById(compId);
    }

    public ResponseCompilationDto getCompilationById(long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() ->
                new CompilationNotFoundException("Compilation with id " + compId + " was not found"));

        ResponseCompilationDto responseCompilationDto = compilationsMapper.toDto(compilation);

        return mapEventList(compilation, responseCompilationDto);
    }

    public ArrayList<ResponseCompilationDto> getCompilations(Boolean pinned, int from, int size) {
        List<Compilation> compilations;

        if (pinned == null) {
            compilations = compilationRepository.findAll(PageRequest.of(from, size)).getContent();
        } else {
            compilations = compilationRepository.findByPinned(pinned, PageRequest.of(from, size)).getContent();
        }

        List<ResponseCompilationDto> responseCompilations = compilations.stream()
                .map(customCompilationMapper::toDto)
                .collect(Collectors.toList());

        return setViewsAndConfirmedRequestsForList(responseCompilations);
    }

    private ArrayList<ResponseCompilationDto> setViewsAndConfirmedRequestsForList(List<ResponseCompilationDto>
                                                                                          responseCompilations) {
        for (ResponseCompilationDto compilation : responseCompilations) {
            compilation.getEventList()
                    .stream()
                    .map(eventShortDto -> setViewsAndConfirmedRequests(eventShortDto))
                    .collect(Collectors.toList());
        }

        return (ArrayList<ResponseCompilationDto>) responseCompilations;
    }

    private ResponseCompilationDto mapEventList(Compilation compilation, ResponseCompilationDto responseCompilationDto) {
        ArrayList<EventShortDto> fullEvents = (ArrayList<EventShortDto>) compilation.getEventList().stream()
                .map(eventMapper::toEventShortDto)
                .map(eventShortDto -> setViewsAndConfirmedRequests(eventShortDto))
                .collect(Collectors.toList());

        responseCompilationDto.setEventList(fullEvents);

        return responseCompilationDto;
    }

    @NonNull
    private BooleanBuilder makeBooleanBuilder(@NonNull EventFilterForCompilations filter) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QEvent.event.id.in(filter.getIds()));

        return builder;
    }

    private Compilation setEvents(Compilation compilation, RequestCompilationDto requestCompilationDto) {
        EventFilterForCompilations filter = EventFilterForCompilations.builder()
                .ids(requestCompilationDto.getEvents()).build();

        BooleanBuilder parameters = makeBooleanBuilder(filter);
        ArrayList<Event> events = (ArrayList<Event>) eventRepository
                .findAll(parameters, PageRequest.of(0, 100)).getContent();

        compilation.setEventList(events);

        return compilation;
    }

    private ArrayList<ViewStats> getStatsList(String start, String end, Collection<String> uris, boolean flag) {
        return statsClient.getStats(start, end, uris, flag);
    }

    private EventShortDto setViewsAndConfirmedRequests(EventShortDto eventShortDto) {
        String uri = "/events/" + eventShortDto.getId();
        String start = eventShortDto.getCreatedOn().format(DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        eventShortDto.setConfirmedRequests(requestRepository.countRequestByStatusEqualsAndEvent_Id
                (RequestStatus.CONFIRMED, eventShortDto.getId()));
        ArrayList<ViewStats> stats = getStatsList(start, end, List.of(uri), false);

        if (stats.size() == 0) {
            eventShortDto.setViews(0L);
        } else {
            eventShortDto.setViews(stats.get(0).getHits());
        }
        return eventShortDto;
    }
}



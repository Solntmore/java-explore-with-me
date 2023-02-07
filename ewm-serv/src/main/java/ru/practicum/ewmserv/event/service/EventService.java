package ru.practicum.ewmserv.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.RequestHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ViewStats;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.dto.*;
import ru.practicum.ewmserv.event.exceptions.EventNotFoundException;
import ru.practicum.ewmserv.event.exceptions.NotAllowToEditEventException;
import ru.practicum.ewmserv.event.mapper.EventMapper;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.event.repository.EventRepository;
import ru.practicum.ewmserv.request.repository.RequestRepository;
import ru.practicum.ewmserv.user.exceptions.UserNotFoundException;
import ru.practicum.ewmserv.user.model.User;
import ru.practicum.ewmserv.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewmserv.constants.Constants.DATE_TIME_FORMATTER;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatsClient statsClient;
    private final RequestRepository requestRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id=" + userId + " was not found"));
        LocalDateTime start = LocalDateTime.now();
        Event event = eventMapper.toEntityFromNewEventDto(newEventDto);
        event.setInitiator(user);
        event.setCreatedOn(start);

        EventFullDto saveEvent = eventMapper.toEventFullDto(eventRepository.save(event));
        saveEvent.setViews(0L);
        saveEvent.setConfirmedRequests(0);

        return saveEvent;
    }

    public EventFullDto getEventById(long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));
        addHit(request);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    public EventFullDto patchEventById(long eventId, UpdateEventAdminDto updateEventAdminDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));


        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new NotAllowToEditEventException("You are not allow event, if before start less then 1 hour");
        }

        if (!event.getStateAction().equals(StateAction.PENDING)) {
            throw new NotAllowToEditEventException("You are not allow event, if stateAction is not Pending");
        }

        EventFullDto eventFullDto = eventMapper.toEventFullDto(
                eventRepository.patchEventByAdmin(eventId, updateEventAdminDto, event));
        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    public EventFullDto patchEventByUserIdAndEventId(long userId, long eventId, EventPatchDto eventPatchDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getStateAction().equals(StateAction.PUBLISHED)) {
            throw new NotAllowToEditEventException("You are not allow event, if stateAction is Published");
        }

        if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate())) {
            throw new NotAllowToEditEventException("You are not edit event, if before start less then 2 hours");
        }

        EventFullDto eventFullDto = eventMapper.toEventFullDto(
                eventRepository.patchEventByUser(userId, eventId, eventPatchDto, event));
        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    public ArrayList<EventShortDto> getEventsByAuthorId(long userId, PageRequest pageRequest) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

        List<Event> events = eventRepository.findAllByInitiator_Id(userId, pageRequest).getContent();

        return (ArrayList<EventShortDto>) events.stream()
                .map(eventMapper::toEventShortDto)
                .map(eventShortDto -> setViewsAndConfirmedRequests(eventShortDto))
                .collect(Collectors.toList());

    }

    public EventFullDto getEventByAuthorIdAndEventId(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Event with id=" + userId + " was not found");
        }

        EventFullDto eventFullDto = eventMapper.toEventFullDto(
                eventRepository.findByIdAndInitiator_Id(eventId, userId));

        if (eventFullDto.getInitiator().getId() != userId) {
            throw new NotAllowToEditEventException("Full info only for initiator");
        }

        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    private EventFullDto setViewsAndConfirmedRequests(EventFullDto eventFullDto, long eventId) {
        String uri = "/events/" + eventId;
        String start = eventFullDto.getCreatedOn().format(DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        eventFullDto.setConfirmedRequests(requestRepository.countRequestByStatusEqualsAndEvent_Id
                (RequestStatus.CONFIRMED, eventId));
        ArrayList<ViewStats> stats = getStatsList(start, end, List.of(uri), false);

        if (stats.size() == 0) {
            eventFullDto.setViews(0L);
        } else {
            eventFullDto.setViews(stats.get(0).getHits());
        }
        return eventFullDto;
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

    private void addHit(HttpServletRequest request) {
        RequestHitDto requestHitDto = new RequestHitDto("ewm-serv", request.getRequestURI(), request.getRemoteUser());
        requestHitDto.setTimestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        statsClient.saveRequest(requestHitDto);
    }

    private ArrayList<ViewStats> getStatsList(String start, String end, Collection<String> uris, boolean flag) {
        return statsClient.getStats(start, end, uris, flag);
    }
/*
    public void getEventsWithParameters(String text, List<Long> categories, Boolean paid, String rangeStart,
                                        String rangeEnd, Boolean onlyAvailable, String sort, PageRequest of) {
        boolean allUsers = true;
        boolean allStates = true;
        boolean allCategories = true;
        boolean allText = true;
        LocalDateTime start;
        LocalDateTime end;

        if (text != null) {
            allText = false;
        }

        if (!categories.isEmpty()) {
            allCategories = false;
        }

        if (rangeStart == null) {
            start = LocalDateTime.now();
            end = start.plusYears(100);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        if (sort == null) {
            sort = "EVENT_DATE";
        }


        eventRepository.getEventWithParametersForAdmin(allUsers, users, );
        /*
        @Query("SELECT e from Event as e " +
            "join fetch e.initiator " +
            "join fetch e.category " +
            "where " +
            "((:allUsers = true) or e.initiator.id in (:users)) and " +
            "((:allStates = true) or e.stateAction in (:states)) and " +
            "((:allCategories = true) or e.category.id in (:categories)) and " +
            "((e.eventDate between :rangeStart and :rangeEnd))")
    Page<Event> getEventWithParameters(boolean allUsers, Collection<Long> users,
                                       boolean allStates, Collection<StateAction> states,
                                       boolean allCategories, Collection<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                       Pageable pageable);
         */
    }


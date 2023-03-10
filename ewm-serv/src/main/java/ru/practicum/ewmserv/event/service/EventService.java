package ru.practicum.ewmserv.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.RequestHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ewmserv.category.exceptions.CategoryNotFoundException;
import ru.practicum.ewmserv.category.model.Category;
import ru.practicum.ewmserv.category.repository.CategoryRepository;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.dto.*;
import ru.practicum.ewmserv.event.exceptions.EventNotFoundException;
import ru.practicum.ewmserv.event.exceptions.NotAllowToEditEventException;
import ru.practicum.ewmserv.event.mapper.EventMapper;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.event.model.QEvent;
import ru.practicum.ewmserv.event.repository.EventRepository;
import ru.practicum.ewmserv.event.util.EventFilterForAdmin;
import ru.practicum.ewmserv.event.util.EventFilterForUser;
import ru.practicum.ewmserv.request.repository.RequestRepository;
import ru.practicum.ewmserv.user.exceptions.UserNotFoundException;
import ru.practicum.ewmserv.user.model.User;
import ru.practicum.ewmserv.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewmserv.configuration.AppConfig.DATE_TIME_FORMATTER;

@RequiredArgsConstructor
@Service
public class EventService {
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public EventFullDto postEvent(long userId, NewEventDto newEventDto) {
        LocalDateTime start = LocalDateTime.now();

        if (newEventDto.getEventDate().isBefore(start)) {
            throw new NotAllowToEditEventException("Event date should be in future");
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id=" + userId + " was not found"));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() ->
                new CategoryNotFoundException("Category was not found"));

        Event event = eventMapper.toEntityFromNewEventDto(newEventDto);
        event.setInitiator(user);
        event.setCreatedOn(start);
        event.setCategory(category);

        if (event.getParticipantLimit() == 0) {
            event.setParticipantLimit(5000000);
        }

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

    public EventFullDto updateEventById(long eventId, UpdateEventAdminDto updateEventAdminDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));
        LocalDateTime now = LocalDateTime.now();

        if (updateEventAdminDto.getEventDate() != null && updateEventAdminDto.getEventDate().isBefore(now)) {
            throw new NotAllowToEditEventException("You are not allow to edit event, if new event date in past");
        }

        if (now.plusHours(1).isAfter(event.getEventDate())) {
            throw new NotAllowToEditEventException("You are not allow to edit event, if before start less then 1 hour");
        }

        if (!event.getState().equals(StateAction.PENDING)) {
            throw new NotAllowToEditEventException("You are not allow to edit event, if stateAction is not Pending");
        }

        if (updateEventAdminDto.getParticipantLimit() != null && updateEventAdminDto.getParticipantLimit() == 0) {
            updateEventAdminDto.setParticipantLimit(5000000);
        }

        EventFullDto eventFullDto = eventMapper.toEventFullDto(
                eventRepository.patchEventByAdmin(eventId, updateEventAdminDto, event));
        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    public EventFullDto patchEventByUserIdAndEventId(long userId, long eventId, EventPatchDto eventPatchDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));
        LocalDateTime now = LocalDateTime.now();

        Optional<Integer> participantLimit = Optional.ofNullable(eventPatchDto.getParticipantLimit());
        Optional<LocalDateTime> eventDate = Optional.ofNullable(eventPatchDto.getEventDate());


        if (event.getState().equals(StateAction.PUBLISHED)) {
            throw new NotAllowToEditEventException("You are not allow event, if stateAction is Published");
        }

        if (now.plusHours(2).isAfter(event.getEventDate())) {
            throw new NotAllowToEditEventException("You are not edit event, if before start less then 2 hours");
        }

        if (eventDate.isPresent() && now.plusHours(2).isAfter(eventDate.get())) {
            throw new NotAllowToEditEventException("You are not edit event, if before start less then 2 hours");
        }

        if (participantLimit.isPresent() && participantLimit.get() == 0) {
            eventPatchDto.setParticipantLimit(5000000);
        }

        EventFullDto eventFullDto = eventMapper.toEventFullDto(
                eventRepository.patchEventByUser(userId, eventId, eventPatchDto, event));
        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    public List<EventShortDto> getEventsByAuthorId(long userId, PageRequest pageRequest) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageRequest).getContent();

        return events.stream()
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
                eventRepository.findByIdAndInitiatorId(eventId, userId));

        if (eventFullDto.getInitiator().getId() != userId) {
            throw new NotAllowToEditEventException("Full info only for initiator");
        }

        return setViewsAndConfirmedRequests(eventFullDto, eventId);
    }

    public List<EventShortDto> getEventsForUser(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                String rangeEnd, Boolean onlyAvailable, String sort, int from,
                                                int size, HttpServletRequest request) {
        addHit(request);
        EventFilterForUser filter = makeUserFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        BooleanBuilder parameters = makeBooleanBuilder(filter);
        List<Event> events = eventRepository.findAll(parameters, PageRequest.of(from, size)).getContent();

        List<EventShortDto> fullEvents = events.stream()
                .map(eventMapper::toEventShortDto)
                .map(eventShortDto -> setViewsAndConfirmedRequests(eventShortDto))
                .collect(Collectors.toList());

        if (sort != null && sort.equals("EVENT_DATE")) {
            fullEvents.sort(Comparator.comparing(EventShortDto::getEventDate));
            return fullEvents;
        }

        if (sort != null && sort.equals("VIEWS")) {
            fullEvents.sort(Comparator.comparing(EventShortDto::getEventDate));
            return fullEvents;
        }
        return fullEvents;
    }

    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<StateAction> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, int from, int size) {
        EventFilterForAdmin filter = makeAdminFilter(users, states, categories, rangeStart, rangeEnd);
        BooleanBuilder parameters = makeBooleanBuilder(filter);
        List<Event> events = eventRepository.findAll(parameters, PageRequest.of(from, size)).getContent();

        return events.stream()
                .map(eventMapper::toEventFullDto)
                .map(eventFullDto -> setViewsAndConfirmedRequests(eventFullDto, eventFullDto.getId()))
                .collect(Collectors.toList());
    }

    private EventFullDto setViewsAndConfirmedRequests(EventFullDto eventFullDto, long eventId) {
        String uri = "/events/" + eventId;
        String start = eventFullDto.getCreatedOn().format(DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        eventFullDto.setConfirmedRequests(requestRepository.countRequestByStatusEqualsAndEventId
                (RequestStatus.CONFIRMED, eventId));
        List<Long> stats = getStatsList(start, end, List.of(uri), false);

        if (stats.size() == 0) {
            eventFullDto.setViews(0L);
        } else {
            eventFullDto.setViews(stats.get(0));
        }
        return eventFullDto;
    }

    private EventShortDto setViewsAndConfirmedRequests(EventShortDto eventShortDto) {
        String uri = "/events/" + eventShortDto.getId();
        String start = eventShortDto.getCreatedOn().format(DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(DATE_TIME_FORMATTER);

        eventShortDto.setConfirmedRequests(requestRepository.countRequestByStatusEqualsAndEventId
                (RequestStatus.CONFIRMED, eventShortDto.getId()));
        List<Long> stats = getStatsList(start, end, List.of(uri), false);

        if (stats.size() == 0) {
            eventShortDto.setViews(0L);
        } else {
            eventShortDto.setViews(stats.get(0));
        }
        return eventShortDto;
    }

    private void addHit(HttpServletRequest request) {
        RequestHitDto requestHitDto = RequestHitDto.builder().app("ewm-serv").uri(request.getRequestURI())
                .ip(request.getRemoteAddr()).timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER)).build();
        statsClient.saveRequest(requestHitDto);
    }

    private List<Long> getStatsList(String start, String end, Collection<String> uris, boolean flag) {
        return statsClient.getStats(start, end, uris, flag);
    }

    private EventFilterForUser makeUserFilter(String text, List<Long> categories, Boolean paid, String rangeStart,
                                              String rangeEnd, Boolean onlyAvailable) {
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null) {
            start = LocalDateTime.now();
            end = start.plusYears(100);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        return EventFilterForUser.builder().text(text).categories(categories)
                .paid(paid).rangeStart(start).rangeEnd(end).onlyAvailable(onlyAvailable).build();
    }

    private EventFilterForAdmin makeAdminFilter(List<Long> users, List<StateAction> states, List<Long> categories,
                                                String rangeStart, String rangeEnd) {
        LocalDateTime start;
        LocalDateTime end;

        if (rangeStart == null) {
            start = LocalDateTime.now();
            end = start.plusYears(100);
        } else {
            start = LocalDateTime.parse(rangeStart, formatter);
            end = LocalDateTime.parse(rangeEnd, formatter);
        }

        return EventFilterForAdmin.builder().users(users).states(states).categories(categories)
                .rangeStart(start).rangeEnd(end).build();
    }

    @NonNull
    private BooleanBuilder makeBooleanBuilder(@NonNull EventFilterForUser filter) {
        BooleanBuilder builder = new BooleanBuilder();
        if (filter.getText() != null) {
            builder.and(QEvent.event.annotation.likeIgnoreCase(filter.getText())
                    .or(QEvent.event.description.likeIgnoreCase(filter.getText())));
        }
        if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
            builder.and(QEvent.event.category.id.in(filter.getCategories()));
        }
        if (filter.getPaid() != null) {
            builder.and(QEvent.event.paid.eq(filter.getPaid()));
        }
        if (filter.getOnlyAvailable() != null) {
            builder.and(QEvent.event.participantLimit.goe(0));
        }

        builder.and(QEvent.event.eventDate.after(filter.getRangeStart()));
        builder.and(QEvent.event.eventDate.before(filter.getRangeEnd()));
        return builder;
    }

    @NonNull
    private BooleanBuilder makeBooleanBuilder(@NonNull EventFilterForAdmin filter) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getUsers() != null && !filter.getUsers().isEmpty()) {
            builder.and(QEvent.event.initiator.id.in(filter.getUsers()));
        }
        if (filter.getStates() != null && !filter.getStates().isEmpty()) {
            builder.and(QEvent.event.state.in(filter.getStates()));
        }
        if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
            builder.and(QEvent.event.category.id.in(filter.getCategories()));
        }

        builder.and(QEvent.event.eventDate.after(filter.getRangeStart()));
        builder.and(QEvent.event.eventDate.before(filter.getRangeEnd()));
        return builder;
    }
}



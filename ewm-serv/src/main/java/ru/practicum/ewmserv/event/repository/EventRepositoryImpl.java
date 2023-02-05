package ru.practicum.ewmserv.event.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.ewmserv.category.model.Category;
import ru.practicum.ewmserv.category.repository.CategoryRepository;
import ru.practicum.ewmserv.enums.AdminState;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.enums.UserState;
import ru.practicum.ewmserv.event.dto.EventPatchDto;
import ru.practicum.ewmserv.event.dto.UpdateEventAdminDto;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.event.model.Location;

import java.time.LocalDateTime;
import java.util.Optional;

public class EventRepositoryImpl implements EventRepositoryCustom {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventRepositoryImpl(@Lazy EventRepository eventRepository, @Lazy CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Event patchEventByAdmin(long eventId, UpdateEventAdminDto updateEvent, Event event) {

        Optional<String> annotation = Optional.ofNullable(updateEvent.getAnnotation());
        Optional<Long> categoryId = Optional.ofNullable(updateEvent.getCategoryId());
        Optional<String> description = Optional.ofNullable(updateEvent.getDescription());
        Optional<Location> location = Optional.ofNullable(updateEvent.getLocation());
        Optional<Boolean> paid = Optional.of(updateEvent.isPaid());
        Optional<Integer> participantLimit = Optional.of(updateEvent.getParticipantLimit());
        Optional<Boolean> requestModeration = Optional.of(updateEvent.isRequestModeration());
        Optional<AdminState> stateAction = Optional.ofNullable(updateEvent.getStateAction());
        Optional<String> title = Optional.ofNullable(updateEvent.getTitle());

        annotation.ifPresent(event::setAnnotation);
        description.ifPresent(event::setDescription);
        location.ifPresent(event::setLocation);
        paid.ifPresent(event::setPaid);
        participantLimit.ifPresent(event::setParticipantLimit);
        requestModeration.ifPresent(event::setRequestModeration);
        title.ifPresent(event::setTitle);

        if (categoryId.isPresent()) {
            Optional<Category> category = categoryRepository.findById(categoryId.get());
            category.ifPresent(event::setCategory);
        }

        if (stateAction.isPresent()) {
            if (stateAction.get() == AdminState.PUBLISH_EVENT) {
                event.setStateAction(StateAction.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                event.setStateAction(StateAction.CANCELED);
            }
        }

        return eventRepository.save(event);
    }

    @Override
    public Event patchEventByUser(long userId, long eventId, EventPatchDto updateEvent, Event event) {
        Optional<String> annotation = Optional.ofNullable(updateEvent.getAnnotation());
        Optional<Long> categoryId = Optional.ofNullable(updateEvent.getCategoryId());
        Optional<String> description = Optional.ofNullable(updateEvent.getDescription());
        Optional<Location> location = Optional.ofNullable(updateEvent.getLocation());
        Optional<Boolean> paid = Optional.of(updateEvent.isPaid());
        Optional<Integer> participantLimit = Optional.of(updateEvent.getParticipantLimit());
        Optional<Boolean> requestModeration = Optional.of(updateEvent.isRequestModeration());
        Optional<UserState> stateAction = Optional.ofNullable(updateEvent.getStateAction());
        Optional<String> title = Optional.ofNullable(updateEvent.getTitle());

        annotation.ifPresent(event::setAnnotation);
        description.ifPresent(event::setDescription);
        location.ifPresent(event::setLocation);
        paid.ifPresent(event::setPaid);
        participantLimit.ifPresent(event::setParticipantLimit);
        requestModeration.ifPresent(event::setRequestModeration);
        title.ifPresent(event::setTitle);

        if (categoryId.isPresent()) {
            Optional<Category> category = categoryRepository.findById(categoryId.get());
            category.ifPresent(event::setCategory);
        }

        if (stateAction.isPresent()) {
            if (stateAction.get() == UserState.CANCEL_REVIEW) {
                event.setStateAction(StateAction.CANCELED);
            }
        }

        return eventRepository.save(event);
    }
}

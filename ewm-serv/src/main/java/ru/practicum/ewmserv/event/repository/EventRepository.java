package ru.practicum.ewmserv.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewmserv.event.dto.EventPatchDto;
import ru.practicum.ewmserv.event.dto.UpdateEventAdminDto;
import ru.practicum.ewmserv.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom,
        QuerydslPredicateExecutor<Event> {
    Event patchEventByAdmin(long eventId, UpdateEventAdminDto updateEvent, Event event);

    Event patchEventByUser(long userId, long eventId, EventPatchDto updateEvent, Event event);

    Page<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

}
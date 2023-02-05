package ru.practicum.ewmserv.event.repository;

import ru.practicum.ewmserv.event.dto.EventPatchDto;
import ru.practicum.ewmserv.event.dto.UpdateEventAdminDto;
import ru.practicum.ewmserv.event.model.Event;

public interface EventRepositoryCustom {
    Event patchEventByAdmin(long eventId, UpdateEventAdminDto updateEvent, Event event);

    Event patchEventByUser(long userId, long eventId, EventPatchDto updateEvent, Event event);
}
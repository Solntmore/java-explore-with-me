package ru.practicum.ewmserv.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
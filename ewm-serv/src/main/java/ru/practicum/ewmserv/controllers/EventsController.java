package ru.practicum.ewmserv.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventsController {
    @GetMapping
    public void getEvents() {
        log.debug("A Get/events request was received. Get events");
    }

    @GetMapping("{eventId}")
    public void getEventsById(@PathVariable long eventId) {
        log.debug("A Get/events/{} request was received. Get event by id", eventId);
    }
}

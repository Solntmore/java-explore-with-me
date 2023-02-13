package ru.practicum.ewmserv.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmserv.event.dto.EventFullDto;
import ru.practicum.ewmserv.event.dto.EventShortDto;
import ru.practicum.ewmserv.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventController {

    private final EventService eventService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable long eventId, HttpServletRequest request) {
        log.debug("A Get/events/{} request was received. Get event by id", eventId);

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventById(eventId, request));
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEvents(@RequestParam(required = false) String text,
                                                              @RequestParam(required = false) List<Long> categories,
                                                              @RequestParam(required = false) Boolean paid,
                                                              @RequestParam(required = false) String rangeStart,
                                                              @RequestParam(required = false) String rangeEnd,
                                                              @RequestParam(required = false) Boolean onlyAvailable,
                                                              @RequestParam(required = false) String sort,
                                                              @RequestParam(required = false, defaultValue = "0") int from,
                                                              @RequestParam(required = false, defaultValue = "10") int size,
                                                              HttpServletRequest request) {
        log.debug("A Get/events request was received. Get events with parameters");
        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsForUser(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size, request));

    }
}

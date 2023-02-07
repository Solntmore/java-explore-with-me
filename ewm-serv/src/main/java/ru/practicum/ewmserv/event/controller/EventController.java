package ru.practicum.ewmserv.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmserv.enums.SortEnum;
import ru.practicum.ewmserv.event.dto.EventFullDto;
import ru.practicum.ewmserv.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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
    }/*
    @GetMapping
    public void getEvents(@RequestParam(required = false) String text,
                          @RequestParam(required = false) List<Long> categories,
                          @RequestParam(required = false) boolean paid,
                          @RequestParam(required = false) String rangeStart,
                          @RequestParam(required = false) String rangeEnd,
                          @RequestParam(required = false) boolean onlyAvailable,
                          @RequestParam(required = false) String sort,
                          @RequestParam(required = false, defaultValue = "0") int from,
                          @RequestParam(required = false, defaultValue = "10") int size) {
        log.debug("A Get/events request was received. Get events with parameters");
        eventService.getEventsWithParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort,
                PageRequest.of(from, size));
    }*/

}

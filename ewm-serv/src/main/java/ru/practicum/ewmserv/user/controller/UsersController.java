package ru.practicum.ewmserv.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UsersController {

    @GetMapping("/{userId}/events")
    public void getEventsByAuthorId(@PathVariable long userId) {
        log.debug("A Get/users/{}/events request was received. Get events posted by author id", userId);
    }

    @PostMapping("/{userId}/events")
    public void postEvent(@PathVariable long userId) {
        log.debug("A Post/users/{}/events request was received. Post event by current user", userId);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public void getInfoByUserAndEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("A Get/users/{}/events/{} request was received. Get info about event by AuthorId and eventId",
                userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public void patchInfoByUserAndEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("A Patch/users/{}/events/{} request was received. Patch info about event by AuthorId and eventId",
                userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public void getInfoAboutRequestsByUserAndEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("A Get/users/{}/events/{}/requests request was received. Get info about requests to event " +
                        "by AuthorId and eventId", userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public void ChangeStatusOfRequestByUserAndEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("A Patch/users/{}/events/{}/requests request was received. Change status for requests to event " +
                        "by AuthorId and eventId", userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public void getInfoAboutUsersRequests(@PathVariable long userId) {
        log.debug("A Get/users/{}/requests request was received. Get info about user requests by user id", userId);
    }

    @PostMapping("/{userId}/requests")
    public void postRequestToTakePartInEvent(@PathVariable long userId) {
        log.debug("A Post/users/{}/requests request was received. Post request to take part in event", userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public void cancelRequestToTakePartInEvent(@PathVariable long userId, @PathVariable long requestId) {
        log.debug("A Patch/users/{}/requests/{}/cancel request was received. " +
                "Cancel request to take part in event", userId, requestId);
    }




}

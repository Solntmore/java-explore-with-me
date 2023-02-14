package ru.practicum.ewmserv.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmserv.comment.dto.CreateCommentDto;
import ru.practicum.ewmserv.comment.dto.PatchCommentDto;
import ru.practicum.ewmserv.comment.dto.ResponseCommentDto;
import ru.practicum.ewmserv.comment.service.CommentService;
import ru.practicum.ewmserv.event.dto.EventFullDto;
import ru.practicum.ewmserv.event.dto.EventPatchDto;
import ru.practicum.ewmserv.event.dto.EventShortDto;
import ru.practicum.ewmserv.event.dto.NewEventDto;
import ru.practicum.ewmserv.event.service.EventService;
import ru.practicum.ewmserv.request.dto.ResponseRequestDto;
import ru.practicum.ewmserv.request.service.RequestServise;
import ru.practicum.ewmserv.user.model.ResultOfUpdateRequests;
import ru.practicum.ewmserv.user.model.UpdateListForRequests;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UsersController {

    private final EventService eventService;

    private final RequestServise requestServise;

    private final CommentService commentService;

    @GetMapping("/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getEventsByAuthorId(@PathVariable long userId,
                                                                   @RequestParam(required = false, defaultValue = "0")
                                                                        int from,
                                                                   @RequestParam(required = false, defaultValue = "10")
                                                                        int size) {
        log.debug("A Get/users/{}/events request was received. Get events posted by author id", userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByAuthorId(userId, PageRequest.of(from, size)));
    }

    @PostMapping("/{userId}/events")
    public ResponseEntity<EventFullDto> postEvent(@PathVariable long userId, @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("A Post/users/{}/events request was received. Post event by current user", userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.postEvent(userId, newEventDto));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getInfoByUserAndEventId(@PathVariable long userId, @PathVariable long eventId) {
        log.debug("A Get/users/{}/events/{} request was received. Get info about event by AuthorId and eventId",
                userId, eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventByAuthorIdAndEventId(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> patchEventByUserAndEventId(@PathVariable long userId, @PathVariable long eventId,
                                                                   @RequestBody EventPatchDto eventPatchDto) {
        log.debug("A Patch/users/{}/events/{} request was received. Patch info about event by AuthorId and eventId",
                userId, eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.patchEventByUserIdAndEventId(userId, eventId, eventPatchDto));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<ArrayList<ResponseRequestDto>> getInfoAboutRequestsByUserAndEventId(@PathVariable long userId,
                                                                                              @PathVariable long eventId) {
        log.debug("A Get/users/{}/events/{}/requests request was received. Get info about requests to event " +
                "by AuthorId and eventId", userId, eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(requestServise.getRequestsForEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<ResultOfUpdateRequests>
    ChangeStatusOfRequestByUserAndEventId(@PathVariable long userId, @PathVariable long eventId,
                                          @RequestBody UpdateListForRequests updateListForRequests) {
        log.debug("A Patch/users/{}/events/{}/requests request was received. Change status for requests to event " +
                "by AuthorId and eventId", userId, eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(requestServise.updateStatusOfRequests(userId, eventId, updateListForRequests));
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<ArrayList<ResponseRequestDto>> getInfoAboutUsersRequests(@PathVariable long userId) {
        log.debug("A Get/users/{}/requests request was received. Get info about user requests by user id", userId);

        return ResponseEntity.status(HttpStatus.OK).body(requestServise.getInfoAboutUserRequest(userId));
    }

    @PostMapping("/{userId}/requests")
    public ResponseEntity<ResponseRequestDto> postRequestToTakePartInEvent(@PathVariable long userId,
                                                                           @RequestParam int eventId) {
        log.debug("A Post/users/{}/requests request was received. Post request to take part in event", userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(requestServise.postRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ResponseRequestDto> cancelRequestToTakePartInEvent(@PathVariable long userId,
                                                                             @PathVariable long requestId) {
        log.debug("A Patch/users/{}/requests/{}/cancel request was received. " +
                "Cancel request to take part in event", userId, requestId);

        return ResponseEntity.status(HttpStatus.OK).body(requestServise.cancelRequest(userId, requestId));
    }

    @PostMapping("/{userId}/comment/{eventId}")
    public ResponseEntity<ResponseCommentDto> createComment(@PathVariable long userId, @PathVariable long eventId,
                                                            @Valid @RequestBody CreateCommentDto comment) {
        log.debug("A Post/users/{}/comment/{} request was received. User {} create comment to event {}", userId, eventId,
                userId, eventId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(userId, eventId, comment));
    }

    @DeleteMapping("/{userId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long userId, @PathVariable long commentId) {
        log.debug("A Delete/users/{}/comment/{} request was received. User {} delete comment {}", userId, commentId,
                userId, commentId);
        commentService.userDeleteComment(userId, commentId);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{userId}/comment/{commentId}")
    public ResponseEntity<ResponseCommentDto> patchComment(@PathVariable long userId, @PathVariable long commentId,
                                                           @Valid @RequestBody PatchCommentDto comment) {
        log.debug("A Patch/users/{}/comment/{} request was received. User {} patch comment {}", userId, commentId,
                userId, commentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateComment(userId, commentId, comment));
    }


}

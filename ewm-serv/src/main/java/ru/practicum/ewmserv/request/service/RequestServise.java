package ru.practicum.ewmserv.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmserv.enums.RequestStatus;
import ru.practicum.ewmserv.enums.RequestUpdateState;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.exceptions.EventNotFoundException;
import ru.practicum.ewmserv.event.model.Event;
import ru.practicum.ewmserv.event.repository.EventRepository;
import ru.practicum.ewmserv.request.dto.ResponseRequestDto;
import ru.practicum.ewmserv.request.exceptions.IncorrectlyRequestException;
import ru.practicum.ewmserv.request.exceptions.NotRequesterIdException;
import ru.practicum.ewmserv.request.exceptions.RequestNotFoundException;
import ru.practicum.ewmserv.request.mapper.RequestMapper;
import ru.practicum.ewmserv.request.model.Request;
import ru.practicum.ewmserv.request.repository.RequestRepository;
import ru.practicum.ewmserv.user.exceptions.NotAllowTakeRequestException;
import ru.practicum.ewmserv.user.exceptions.UserNotFoundException;
import ru.practicum.ewmserv.user.model.ResultOfUpdateRequests;
import ru.practicum.ewmserv.user.model.UpdateListForRequests;
import ru.practicum.ewmserv.user.model.User;
import ru.practicum.ewmserv.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServise {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;

    public ResponseRequestDto postRequest(long userId, long eventId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException("User with id=" + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));

        if (userId == event.getInitiator().getId()) {
            throw new NotAllowTakeRequestException("Initiator not allow to be a requester");
        }

        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new NotAllowTakeRequestException("Requester not allow to make more then 1 request");
        }

        if (!event.getState().equals(StateAction.PUBLISHED)) {
            throw new NotAllowTakeRequestException("Requester not allow to make a request on not published event");
        }

        if (event.getParticipantLimit() == 0) {
            throw new NotAllowTakeRequestException("Sorry, but the limit of participants is exhausted");
        }

        Request request = new Request();
        request.setCreated(LocalDateTime.now());
        request.setRequester(user);
        request.setEvent(event);

        if (!event.isRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setParticipantLimit(event.getParticipantLimit() - 1);
            eventRepository.save(event);
        }

        Request saveRequest = requestRepository.save(request);


        return requestMapper.toDto(saveRequest);
    }

    public ResponseRequestDto cancelRequest(long userId, long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new RequestNotFoundException("Request with id=" + requestId + " was not found"));

        if (request.getRequester().getId() != userId) {
            throw new NotRequesterIdException("Only requester or initiator allow to cancel request");
        }

        request.setStatus(RequestStatus.CANCELED);

        return requestMapper.toDto(requestRepository.save(request));
    }

    public ArrayList<ResponseRequestDto> getInfoAboutUserRequest(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

        return (ArrayList<ResponseRequestDto>) requestRepository
                .findAllByRequester_Id(userId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    public ArrayList<ResponseRequestDto> getRequestsForEvent(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getInitiator().getId() != userId) {
            throw new NotAllowTakeRequestException("Only initiator of event allow to check requests");
        }

        return (ArrayList<ResponseRequestDto>) requestRepository
                .findAllByEvent_Id(eventId)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResultOfUpdateRequests updateStatusOfRequests(long userId, long eventId,
                                                         UpdateListForRequests updateListForRequests) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with id=" + userId + " was not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new EventNotFoundException("Event with id=" + eventId + " was not found"));

        ArrayList<Long> ids = updateListForRequests.getRequestIds();
        RequestUpdateState state = updateListForRequests.getStatus();
        ArrayList<Request> confirmedRequests = new ArrayList<>();
        ArrayList<Request> rejectedRequests = new ArrayList<>();

        for (Long id : ids) {
            if (event.getParticipantLimit() == 0) {
                throw new NotAllowTakeRequestException("Sorry, but the limit of participants is exhausted");
            }

            Request request = requestRepository.findByIdAndEvent_Id(id, eventId).orElseThrow(() ->
                    new RequestNotFoundException("Request with id=" + id + " for event with id=" + " was not found"));

            if (!request.getStatus().equals(RequestStatus.PENDING)) {
                throw new IncorrectlyRequestException("Request status is not Pending");
            }

            if (state.equals(RequestUpdateState.CONFIRMED)) {
                request.setStatus(RequestStatus.CONFIRMED);
                confirmedRequests.add(requestRepository.save(request));
                event.setParticipantLimit(event.getParticipantLimit() - 1);
            } else {
                request.setStatus(RequestStatus.REJECTED);
                rejectedRequests.add(requestRepository.save(request));
            }
        }
        eventRepository.save(event);

        return makeResult(confirmedRequests, rejectedRequests);
    }

    private ResultOfUpdateRequests makeResult(ArrayList<Request> confirmedRequests,
                                              ArrayList<Request> rejectedRequests) {

        ArrayList<ResponseRequestDto> confirmed = (ArrayList<ResponseRequestDto>) confirmedRequests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
        ArrayList<ResponseRequestDto> rejected = (ArrayList<ResponseRequestDto>) rejectedRequests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());

        return new ResultOfUpdateRequests(confirmed, rejected);
    }
}

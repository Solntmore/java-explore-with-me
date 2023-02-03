package ru.practicum.ewmserv.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserv.event.mapper.EventMapper;
import ru.practicum.ewmserv.event.repository.EventRepository;
import ru.practicum.ewmserv.user.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventMapper eventMapper;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;



}

package ru.practicum.statservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.RequestHitDto;
import ru.practicum.ResponseHitDto;
import ru.practicum.statservice.service.StatsService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/hit")
public class HitController {

    private final StatsService statsService;

    @PostMapping
    public ResponseEntity<ResponseHitDto> saveRequest(@RequestBody RequestHitDto requestHitDto) {
        log.debug("A Post/stats request was received. Save info about request: {}", requestHitDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(statsService.saveRequest(requestHitDto));
    }
}

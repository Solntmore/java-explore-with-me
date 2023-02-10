package ru.practicum.statservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ViewStats;
import ru.practicum.statservice.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/stats")
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ArrayList<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                             LocalDateTime start,
                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                             LocalDateTime end,
                                                         @RequestParam (defaultValue = "") Collection<String> uris,
                                                         @RequestParam (defaultValue = "false") boolean unique) {
        log.debug("A Get/stats?start={}&end={}&uris={}&flag={} request was received. Get stats about uris: {}", start,
                end, uris, unique, uris);

        return statsService.getStats(start, end, unique, uris);
    }
}

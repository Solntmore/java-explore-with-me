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
@RequestMapping(path = "/compilations")
public class CompilationsController {

    @GetMapping
    public void getCompilations() {
        log.debug("A Get/compilations request was received. Get compilations");
    }

    @GetMapping("{compId}")
    public void getCompilationById(@PathVariable long catId) {
        log.debug("A Get/compilations/{compId} request was received. Get compilation by id", catId);
    }
}

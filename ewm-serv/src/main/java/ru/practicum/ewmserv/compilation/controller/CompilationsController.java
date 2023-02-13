package ru.practicum.ewmserv.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmserv.compilation.dto.ResponseCompilationDto;
import ru.practicum.ewmserv.compilation.service.CompilationsService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping
    public ResponseEntity<List<ResponseCompilationDto>> getCompilations(
            @RequestParam(required = false, defaultValue = "false") Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size) {
        log.debug("A Get/compilations request was received. Get compilations");

        return ResponseEntity.status(HttpStatus.OK).body(compilationsService.getCompilations(pinned, from, size));
    }

    @GetMapping("{compId}")
    public ResponseEntity<ResponseCompilationDto> getCompilationById(@PathVariable long compId) {
        log.debug("A Get/compilations/{} request was received. Get compilation by id", compId);

        return ResponseEntity.status(HttpStatus.OK).body(compilationsService.getCompilationById(compId));
    }
}

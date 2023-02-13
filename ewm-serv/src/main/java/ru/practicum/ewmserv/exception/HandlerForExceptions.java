package ru.practicum.ewmserv.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HandlerForExceptions {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.warn(e.getMessage());
        final var error = HttpStatus.CONFLICT;
        ApiError apiError = ApiError.builder()
                .reason("Integrity constraint has been violated")
                .status(error)
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(error).body(apiError);

    }
}

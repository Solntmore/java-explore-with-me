package ru.practicum.ewmserv.event.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFilterForUser {

    private String text;

    private List<Long> categories;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Boolean onlyAvailable;
}


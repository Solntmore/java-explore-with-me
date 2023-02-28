package ru.practicum.statservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.RequestHitDto;
import ru.practicum.ResponseHitDto;
import ru.practicum.ViewStats;
import ru.practicum.statservice.mappers.HitMapper;
import ru.practicum.statservice.model.Hit;
import ru.practicum.statservice.repository.HitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final HitMapper hitMapper;
    private final HitRepository hitRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public ResponseHitDto saveRequest(RequestHitDto requestHitDto) {
        Hit hit = hitMapper.toHitEntity(requestHitDto);
        LocalDateTime created = LocalDateTime.parse(requestHitDto.getTimestamp(), formatter);
        hit.setCreated(created);
        return hitMapper.toDto(hitRepository.save(hit));
    }

    public ArrayList<ViewStats> getStats(LocalDateTime start, LocalDateTime end, boolean unique,
                                         Collection<String> uris) {
        if (uris.isEmpty()) {
            if (unique) {

                return (ArrayList<ViewStats>) hitRepository.getUniqueHitsBetweenDays(start, end)
                        .stream()
                        .map(hitMapper::toViewStatsEntity)
                        .collect(Collectors.toList());
            }

            return (ArrayList<ViewStats>) hitRepository
                    .getHitsBetweenDays(start, end)
                    .stream()
                    .map(hitMapper::toViewStatsEntity)
                    .collect(Collectors.toList());
        }

        if (unique) {

            return (ArrayList<ViewStats>) hitRepository
                    .getHitsUniqueBetweenDaysFromUriList(start, end, uris)
                    .stream()
                    .map(hitMapper::toViewStatsEntity)
                    .collect(Collectors.toList());
        }

        return (ArrayList<ViewStats>) hitRepository
                .getHitsBetweenDaysFromUriList(start, end, uris)
                .stream()
                .map(hitMapper::toViewStatsEntity)
                .collect(Collectors.toList());
    }
}

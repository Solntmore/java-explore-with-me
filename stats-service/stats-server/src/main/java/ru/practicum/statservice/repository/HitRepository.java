package ru.practicum.statservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.IViewStats;
import ru.practicum.statservice.model.Hit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT h.app as app, h.uri as uri, COUNT(h.ip) as hits " +
            "FROM Hit as h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<IViewStats> getHitsBetweenDays(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(DISTINCT h.ip) as hits " +
            "FROM Hit as h " +
            "WHERE h.created BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<IViewStats> getUniqueHitsBetweenDays(LocalDateTime start, LocalDateTime end);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(h.ip) as hits " +
            "FROM Hit as h " +
            "WHERE h.created BETWEEN :start AND :end AND uri in (:uris) " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<IViewStats> getHitsBetweenDaysFromUriList(LocalDateTime start, LocalDateTime end,
                                                   Collection<String> uris);

    @Query("SELECT h.app as app, h.uri as uri, COUNT(DISTINCT h.ip) as hits " +
            "FROM Hit as h " +
            "WHERE h.created BETWEEN :start AND :end AND uri in (:uris) " +
            "GROUP BY app, uri " +
            "ORDER BY hits DESC")
    List<IViewStats> getHitsUniqueBetweenDaysFromUriList(LocalDateTime start, LocalDateTime end,
                                                         Collection<String> uris);
}
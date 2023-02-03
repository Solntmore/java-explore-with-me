package ru.practicum.ewmserv.event.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annotation", length = 2000, nullable = false)
    @Size(min = 20, max = 2000)
    private String annotation;

    @Column(name = "category_id")
    private int category;

    @Column(name = "description", length = 7000, nullable = false)
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "eventDate", nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    private Location location;

    @Column(name = "paid")
    private boolean paid = false;

    @Column(name = "participantLimit")
    private int participantLimit = 5000000;

    @Column(name = "requestModeration")
    private boolean requestModeration = true;

    @Column(name = "title", length = 120, nullable = false)
    @Size(min = 3, max = 120)
    private String title;



}

package ru.practicum.ewmserv.event.model;

import lombok.*;
import ru.practicum.ewmserv.category.model.Category;
import ru.practicum.ewmserv.compilation.model.Compilation;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description", length = 7000, nullable = false)
    @Size(min = 20, max = 7000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User initiator;

    @Embedded
    private Location location;

    @Column(name = "paid")
    private boolean paid = false;

    @Column(name = "participant_limit")
    private int participantLimit = 5000000;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration = true;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private StateAction state = StateAction.PENDING;

    @Column(name = "title", length = 120, nullable = false)
    @Size(min = 3, max = 120)
    private String title;

    @ManyToMany(mappedBy = "eventList")
    private Set<Compilation> compilations;


}

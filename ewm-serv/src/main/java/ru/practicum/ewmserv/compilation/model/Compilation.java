package ru.practicum.ewmserv.compilation.model;

import lombok.*;
import ru.practicum.ewmserv.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinColumn(name = "event_id")
    private List<Event> eventList;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title")
    private String title;
}

package ru.practicum.ewmserv.compilation.model;

import lombok.*;
import ru.practicum.ewmserv.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Compilations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private ArrayList<Event> eventList;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title")
    private String title;
}

package ru.practicum.ewmserv.compilation.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
/*import org.hibernate.annotations.CascadeType;*/
import ru.practicum.ewmserv.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


        @ManyToMany(cascade = { CascadeType.ALL })
        @JoinTable(
                name = "compilations_events",
                joinColumns = { @JoinColumn(name = "compilation_id") },
                inverseJoinColumns = { @JoinColumn(name = "event_id") }
        )
    /*@ManyToMany
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "event_id")*/
    private List<Event> eventList;

    @Column(name = "pinned")
    private boolean pinned;

    @Column(name = "title")
    private String title;
}

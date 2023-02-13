package ru.practicum.ewmserv.user.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 512, nullable = false, unique = true)
    private String email;

    @Column(name = "name", length = 512, nullable = false)
    private String name;


}

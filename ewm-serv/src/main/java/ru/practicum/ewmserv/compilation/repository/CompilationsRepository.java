package ru.practicum.ewmserv.compilation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.compilation.model.Compilations;

public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
}
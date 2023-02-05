package ru.practicum.ewmserv.category.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.category.model.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    boolean existsById(Long catId);

    @Override
    void deleteById(Long catId);

    @Override
    Optional<Category> findById(Long catId);

    @Override
    Page<Category> findAll(Pageable pageable);
}
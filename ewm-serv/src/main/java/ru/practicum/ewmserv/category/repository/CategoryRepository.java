package ru.practicum.ewmserv.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmserv.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    boolean existsById(Long catId);

    @Override
    void deleteById(Long catId);
}
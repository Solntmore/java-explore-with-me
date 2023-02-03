package ru.practicum.ewmserv.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmserv.category.dto.RequestCategoryDto;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.category.exceptions.CategoryNotFoundException;
import ru.practicum.ewmserv.category.mapper.CategoryMapper;
import ru.practicum.ewmserv.category.model.Category;
import ru.practicum.ewmserv.category.repository.CategoryRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    public ResponseCategoryDto postCategory(RequestCategoryDto requestCategoryDto) {
        Category category = categoryRepository.save(
                categoryMapper.toEntity(requestCategoryDto));

        return categoryMapper.toDto(category);
    }

    public void deleteCategory(long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new CategoryNotFoundException("Category with id=" + catId + " was not found");
        }
        categoryRepository.deleteById(catId);
    }

    public ResponseCategoryDto patchCategory(long catId, RequestCategoryDto requestCategoryDto) {
        Optional<Category> category = categoryRepository.findById(catId);

        if (category.isPresent()) {
            Category newCategory = categoryMapper.partialUpdate(requestCategoryDto, category.get());
            categoryRepository.save(newCategory);

            return categoryMapper.toDto(newCategory);
        }
        throw new CategoryNotFoundException("Category with id=" + catId + " was not found");
    }
}

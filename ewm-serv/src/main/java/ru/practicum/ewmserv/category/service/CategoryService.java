package ru.practicum.ewmserv.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.RequestHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ewmserv.category.dto.RequestCategoryDto;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.category.exceptions.CategoryNotFoundException;
import ru.practicum.ewmserv.category.exceptions.SqlConstraintViolationException;
import ru.practicum.ewmserv.category.mapper.CategoryMapper;
import ru.practicum.ewmserv.category.model.Category;
import ru.practicum.ewmserv.category.repository.CategoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public ResponseCategoryDto postCategory(RequestCategoryDto requestCategoryDto) {
        try {
            Category category = categoryRepository.save(
                    categoryMapper.toEntity(requestCategoryDto));

            return categoryMapper.toDto(category);
        } catch (RuntimeException e) {
            throw new SqlConstraintViolationException(e.getMessage());
        }
    }

    public void deleteCategory(long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new CategoryNotFoundException("Category with id=" + catId + " was not found");
        }

        try {
            categoryRepository.deleteById(catId);
        } catch (RuntimeException e) {
            throw new SqlConstraintViolationException(e.getMessage());
        }
    }

    public ResponseCategoryDto patchCategory(long catId, RequestCategoryDto requestCategoryDto) {
        Optional<Category> category = categoryRepository.findById(catId);

        if (category.isPresent()) {
            Category newCategory = categoryMapper.partialUpdate(requestCategoryDto, category.get());
            try {
                categoryRepository.save(newCategory);
            } catch (RuntimeException e) {
                throw new SqlConstraintViolationException(e.getMessage());
            }
            return categoryMapper.toDto(newCategory);
        }
        throw new CategoryNotFoundException("Category with id=" + catId + " was not found");
    }

    public ResponseCategoryDto getCategoryById(long catId, HttpServletRequest request) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new CategoryNotFoundException("The category with id " + catId + " is not registered."));
        addHit(request);

        return categoryMapper.toDto(category);
    }

    public ArrayList<ResponseCategoryDto> getCategories(PageRequest pageRequest, HttpServletRequest request) {
        addHit(request);
        List<Category> categories = categoryRepository.findAll(pageRequest).getContent();

        return (ArrayList<ResponseCategoryDto>) categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    private void addHit(HttpServletRequest request) {
        RequestHitDto requestHitDto = RequestHitDto.builder().app("ewm-serv").uri(request.getRequestURI())
                .ip(request.getRemoteAddr()).timestamp(LocalDateTime.now().format(formatter)).build();
        statsClient.saveRequest(requestHitDto);

    }
}

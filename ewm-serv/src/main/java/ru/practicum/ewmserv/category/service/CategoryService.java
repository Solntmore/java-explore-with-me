package ru.practicum.ewmserv.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.RequestHitDto;
import ru.practicum.StatsClient;
import ru.practicum.ewmserv.category.dto.RequestCategoryDto;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.category.exceptions.CategoryNotFoundException;
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
        RequestHitDto requestHitDto = new RequestHitDto("ewm-serv", request.getRequestURI(), request.getRemoteAddr());
        requestHitDto.setTimestamp(LocalDateTime.now().format(formatter));
        statsClient.saveRequest(requestHitDto);
    }
}

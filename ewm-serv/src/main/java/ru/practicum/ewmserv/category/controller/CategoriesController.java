package ru.practicum.ewmserv.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ArrayList<ResponseCategoryDto>> getCategories(@RequestParam(required = false, defaultValue = "0")
                                                                            int from,
                                                                        @RequestParam(required = false, defaultValue = "10")
                                                                            int size,
                                                                        HttpServletRequest request) {
        log.debug("A Get/categories request was received. Get category");

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategories(PageRequest.of(from, size), request));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<ResponseCategoryDto> getCategoryById(@PathVariable long catId, HttpServletRequest request) {
        log.debug("A Get/categories/{} request was received. Get category by id", catId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getCategoryById(catId, request));
    }
}

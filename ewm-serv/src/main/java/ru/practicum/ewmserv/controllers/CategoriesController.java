package ru.practicum.ewmserv.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesController {

    @GetMapping
    public void getCategories() {
        log.debug("A Get/categories request was received. Get category");
    }

    @GetMapping("{catId}")
    public void getCategoryById(@PathVariable long catId) {
        log.debug("A Get/categories/{} request was received. Get category by id", catId);
    }
}

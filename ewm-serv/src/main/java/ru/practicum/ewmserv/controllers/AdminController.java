package ru.practicum.ewmserv.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {


    @PostMapping
    public void postCategory() {
        log.debug("A Post/admin request was received. Post category");
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategory(@PathVariable long catId) {
        log.debug("A Delete/admin/categories/{} request was received. Delete category", catId);
    }

    @PatchMapping("/categories/{catId}")
    public void patchCategory(@PathVariable long catId) {
        log.debug("A Patch/admin/categories/{} request was received. Patch category", catId);
    }

    @GetMapping("/events")
    public void getEvents() {
        log.debug("A Get/admin/events request was received. Get events");
    }

    @GetMapping("/events/{id}")
    public void getEventsById(@PathVariable long id) {
        log.debug("A Get/admin/events/{} request was received. Delete category", id);
    }

    @GetMapping("/users")
    public void getUsers() {
        log.debug("A Get/admin/users request was received. Get events");
    }

    @PostMapping("/users")
    public void postUser() {
        log.debug("A Post/admin/users request was received. Post events");
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.debug("A Delete/admin/users/{} request was received. Delete category", userId);
    }

    @PostMapping("/compilations")
    public void postCompilations(){
        log.debug("A Post/admin/compilations request was received. Post compilations");
    }

    @DeleteMapping("/compilations/{compId}")
    public void postCompilations(@PathVariable long compId){
        log.debug("A Delete/admin/compilations/{} request was received. Post compilations", compId);
    }

    @PatchMapping("/compilations/{compId}")
    public void patchCompilations(@PathVariable long compId){
        log.debug("A Patch/admin/compilations/{} request was received. Patch compilations", compId);
    }






}

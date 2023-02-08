package ru.practicum.ewmserv.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmserv.category.dto.RequestCategoryDto;
import ru.practicum.ewmserv.category.dto.ResponseCategoryDto;
import ru.practicum.ewmserv.category.service.CategoryService;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.dto.EventFullDto;
import ru.practicum.ewmserv.event.dto.UpdateEventAdminDto;
import ru.practicum.ewmserv.event.service.EventService;
import ru.practicum.ewmserv.user.dto.RequestUserDto;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;
import ru.practicum.ewmserv.user.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;

    @PostMapping("/categories")
    public ResponseEntity<ResponseCategoryDto> postCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.debug("A Post/admin request was received. Post category");

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.postCategory(requestCategoryDto));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long catId) {
        log.debug("A Delete/admin/categories/{} request was received. Delete category", catId);
        categoryService.deleteCategory(catId);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<ResponseCategoryDto> patchCategory(@PathVariable long catId,
                                                             @RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.debug("A Patch/admin/categories/{} request was received. Patch category", catId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.patchCategory(catId, requestCategoryDto));
    }

    @GetMapping("/events")
    public ResponseEntity<ArrayList<EventFullDto>> getEvents(@RequestParam(required = false) List<Long> users,
                                                             @RequestParam(required = false) List<StateAction> states,
                                                             @RequestParam(required = false) List<Long> categories,
                                                             @RequestParam(required = false) String rangeStart,
                                                             @RequestParam(required = false) String rangeEnd,
                                                             @RequestParam(required = false, defaultValue = "0") int from,
                                                             @RequestParam(required = false, defaultValue = "10") int size) {
        log.debug("A Get/admin/events request was received. Get events");

        return ResponseEntity.status(HttpStatus.OK).body(eventService.getEventsForAdmin(users, states, categories,
                rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto patchEventById(@PathVariable long eventId, @RequestBody UpdateEventAdminDto updateEventAdminDto) {
        log.debug("A Patch/admin/events/{} request was received. Patch event by id", eventId);

        return eventService.patchEventById(eventId, updateEventAdminDto);
    }

    @GetMapping("/users")
    public ResponseEntity<ArrayList<ResponseUserDto>> getUsers(@RequestParam(name = "ids") Integer[] ids,
                                                               @RequestParam(required = false, defaultValue = "0") int from,
                                                               @RequestParam(required = false, defaultValue = "10")
                                                               int size) {
        log.debug("A Get/admin/users request was received. Get users with ids {}", ids);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(ids, PageRequest.of(from, size)));
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUserDto> postUser(@RequestBody @Valid RequestUserDto requestUserDto) {
        log.debug("A Post/admin/users request was received. Post user {}", requestUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.postUser(requestUserDto));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) {
        log.debug("A Delete/admin/users/{} request was received. Delete user", userId);
        userService.deleteUser(userId);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/compilations")
    public void postCompilations() {
        log.debug("A Post/admin/compilations request was received. Post compilations");
    }

    @DeleteMapping("/compilations/{compId}")
    public void postCompilations(@PathVariable long compId) {
        log.debug("A Delete/admin/compilations/{} request was received. Post compilations", compId);
    }

    @PatchMapping("/compilations/{compId}")
    public void patchCompilations(@PathVariable long compId) {
        log.debug("A Patch/admin/compilations/{} request was received. Patch compilations", compId);
    }


}

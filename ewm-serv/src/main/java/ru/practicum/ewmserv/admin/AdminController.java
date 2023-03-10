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
import ru.practicum.ewmserv.comment.service.CommentService;
import ru.practicum.ewmserv.compilation.dto.RequestCompilationDto;
import ru.practicum.ewmserv.compilation.dto.ResponseCompilationDto;
import ru.practicum.ewmserv.compilation.service.CompilationsService;
import ru.practicum.ewmserv.enums.StateAction;
import ru.practicum.ewmserv.event.dto.EventFullDto;
import ru.practicum.ewmserv.event.dto.UpdateEventAdminDto;
import ru.practicum.ewmserv.event.service.EventService;
import ru.practicum.ewmserv.user.dto.RequestUserDto;
import ru.practicum.ewmserv.user.dto.ResponseUserDto;
import ru.practicum.ewmserv.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CommentService commentService;
    private final CompilationsService compilationsService;

    @PostMapping("/categories")
    public ResponseEntity<ResponseCategoryDto> addCategory(@RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.debug("A Post/admin request was received. Post category");

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.addCategory(requestCategoryDto));
    }

    @DeleteMapping("/categories/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable long catId) {
        log.debug("A Delete/admin/categories/{} request was received. Delete category", catId);
        categoryService.deleteCategory(catId);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/categories/{catId}")
    public ResponseEntity<ResponseCategoryDto> updateCategory(@PathVariable long catId,
                                                              @RequestBody @Valid RequestCategoryDto requestCategoryDto) {
        log.debug("A Patch/admin/categories/{} request was received. Patch category", catId);

        return ResponseEntity.status(HttpStatus.OK).body(categoryService.updateCategory(catId, requestCategoryDto));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventFullDto>> getEvents(@RequestParam(required = false) List<Long> users,
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
    public EventFullDto updateEventById(@PathVariable long eventId, @RequestBody UpdateEventAdminDto updateEventAdminDto) {
        log.debug("A Patch/admin/events/{} request was received. Patch event by id", eventId);

        return eventService.updateEventById(eventId, updateEventAdminDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUserDto>> getUsers(@RequestParam(name = "ids") Integer[] ids,
                                                          @RequestParam(required = false, defaultValue = "0") int from,
                                                          @RequestParam(required = false, defaultValue = "10")
                                                          int size) {
        log.debug("A Get/admin/users request was received. Get users with ids {}", ids);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(ids, PageRequest.of(from, size)));
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUserDto> addUser(@RequestBody @Valid RequestUserDto requestUserDto) {
        log.debug("A Post/admin/users request was received. Post user {}", requestUserDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(requestUserDto));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) {
        log.debug("A Delete/admin/users/{} request was received. Delete user", userId);
        userService.deleteUser(userId);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/compilations")
    public ResponseEntity<ResponseCompilationDto> addCompilations(
            @RequestBody @Valid RequestCompilationDto requestCompilationDto) {
        log.debug("A Post/admin/compilations request was received. Post compilations");


        return ResponseEntity.status(HttpStatus.CREATED).body(compilationsService.addCompilation(requestCompilationDto));

    }

    @DeleteMapping("/compilations/{compId}")
    public ResponseEntity<Void> deleteCompilations(@PathVariable long compId) {
        log.debug("A Delete/admin/compilations/{} request was received. Post compilations", compId);
        compilationsService.deleteCompilation(compId);

        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/compilations/{compId}")
    public ResponseEntity<ResponseCompilationDto> patchCompilations(
            @PathVariable long compId, @RequestBody RequestCompilationDto requestCompilationDto) {
        log.debug("A Patch/admin/compilations/{} request was received. Patch compilations", compId);

        return ResponseEntity.status(HttpStatus.OK).body(
                compilationsService.updateCompilation(compId, requestCompilationDto));
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable long commentId) {
        log.debug("A Delete/admin/comment/{} request was received. Admin delete comment {}", commentId, commentId);
        commentService.adminDeleteComment(commentId);

        return ResponseEntity.status(204).build();
    }
}


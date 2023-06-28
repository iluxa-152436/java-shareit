package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ApiErrorMessage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.exception.ConflictException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping
    public User add(@RequestBody @Valid UserDto userDto) {
        User user = mapper.toEntity(userDto);
        return userService.addNewUser(user);
    }

    @GetMapping("/{userId}")
    public User get(@PathVariable long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public User get(@PathVariable long userId, @RequestBody @Valid UserPatchDto userPatchDto) {
        User oldUser = userService.getUserById(userId);
        User newUser = mapper.toEntity(userPatchDto, oldUser);
        return userService.updateUser(newUser);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ApiErrorMessage> handleException(Exception exception) {
        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorMessage> handleExceptionDefaultResponse(MethodArgumentNotValidException exception) {
        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorMessage(exception.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<ApiErrorMessage> handleConflictException(Exception exception) {
        log.debug("Получен статус 409 Conflict {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorMessage(exception.getMessage()));
    }
}

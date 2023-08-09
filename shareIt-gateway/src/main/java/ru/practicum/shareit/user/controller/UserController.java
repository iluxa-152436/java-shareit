package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid UserDto userDto) {
        return client.postUser(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> get(@PathVariable long userId) {
        return client.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.getAllUsers();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patch(@PathVariable long userId, @RequestBody @Valid UserPatchDto userPatchDto) {
        return client.patchUser(userId, userPatchDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        client.deleteUserById(userId);
    }
}

package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User add(@RequestBody UserDto userDto) {
        return userService.addNewUser(userDto);
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
    public User patch(@PathVariable long userId, @RequestBody UserPatchDto userPatchDto) {
        return userService.updateUser(userId, userPatchDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}

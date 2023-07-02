package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
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
    public User patch(@PathVariable long userId, @RequestBody @Valid UserPatchDto userPatchDto) {
        return userService.updateUser(userId, userPatchDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }
}

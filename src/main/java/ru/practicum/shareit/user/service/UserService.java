package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

public interface UserService {
    User addNewUser(User user);

    User updateUser(long userId, UserPatchDto userPatchDto);

    User getUserById(long userId);

    List<User> getAllUsers();

    void deleteUserById(long userId);

    boolean isValidUser(long userId);
}

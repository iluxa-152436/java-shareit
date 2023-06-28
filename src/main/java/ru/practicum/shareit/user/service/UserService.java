package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User addNewUser(User user);

    User updateUser(User user);

    User getUserById(long id);

    List<User> getAllUsers();

    void deleteUserById(long id);

    boolean isValid(long id);
}

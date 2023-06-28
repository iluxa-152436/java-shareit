package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    public User addNewUser(User user) {
        return storage.save(user);
    }

    @Override
    public User getUserById(long id) {
        return storage.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return storage.findAll();
    }

    @Override
    public void deleteUserById(long id) {
        storage.deleteById(id);
    }

    @Override
    public User updateUser(User user) {
        return storage.update(user);
    }

    @Override
    public boolean isValid(long id) {
        return storage.findById(id) != null;
    }
}

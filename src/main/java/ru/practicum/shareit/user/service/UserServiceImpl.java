package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.exception.UserDoesNotExistException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.storage.DbUserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final DbUserStorage storage;
    private final UserMapper mapper;

    @Override
    public User addNewUser(User user) {
        log.debug("Получен user = {}", user);
        return storage.save(user);
    }

    @Override
    public User getUserById(long id) {
        return storage.findById(id)
                .orElseThrow(() -> new UserDoesNotExistException("User with id " + id + " doesn't exist"));
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
    public User updateUser(long userId, UserPatchDto userPatchDto) {
        User newUser = UserMapper.toEntity(userPatchDto, getUserById(userId));
        log.debug("Service");
        log.debug("Получен newUser = {}", newUser);
        return storage.save(newUser);
    }

    @Override
    public boolean isValidUser(long id) {
        return storage.findById(id).isPresent();
    }
}

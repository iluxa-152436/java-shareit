package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Autowired
    public UserServiceImpl(UserStorage storage) {
        this.storage = storage;
    }

    @Transactional
    @Override
    public User addNewUser(UserDto userDto) {
        log.debug("Получен user = {}", userDto);
        return storage.save(UserMapper.toEntity(userDto));
    }

    @Override
    public User getUserById(long id) {
        return storage.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " doesn't exist"));
    }

    @Override
    public List<User> getAllUsers() {
        return storage.findAll();
    }

    @Override
    public void deleteUserById(long id) {
        storage.deleteById(id);
    }

    @Transactional
    @Override
    public User updateUser(long userId, UserPatchDto userPatchDto) {
        User newUser = UserMapper.toEntity(userPatchDto, getUserById(userId));
        log.debug("Service");
        log.debug("Получен newUser = {}", newUser);
        return storage.save(newUser);
    }

    @Override
    public void checkUser(long id) {
        if (storage.findById(id).isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " doesn't exist");
        }
    }
}

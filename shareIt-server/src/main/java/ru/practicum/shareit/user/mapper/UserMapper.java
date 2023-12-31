package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.entity.User;

import java.util.Optional;

public class UserMapper {
    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static User toEntity(UserPatchDto userPatchDto, User user) {
        User newUser = new User(user);
        Optional.ofNullable(userPatchDto.getName()).ifPresent(newUser::setName);
        Optional.ofNullable(userPatchDto.getEmail()).ifPresent(newUser::setEmail);
        return newUser;
    }

    public static UserShortDto toShortDto(User user) {
        UserShortDto userShortDto = new UserShortDto();
        userShortDto.setId(user.getId());
        return userShortDto;
    }
}

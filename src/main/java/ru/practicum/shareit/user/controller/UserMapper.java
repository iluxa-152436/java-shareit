package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final ModelMapper modelMapper;

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public User toEntity(UserPatchDto userPatchDto, User user) {
        User newUser = new User(user);
        if (userPatchDto.getName() != null) {
            newUser.setName(userPatchDto.getName());
        }
        if (userPatchDto.getEmail() != null) {
            newUser.setEmail(userPatchDto.getEmail());
        }
        return newUser;
    }
}

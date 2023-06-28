package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Exclude
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

    public User(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}

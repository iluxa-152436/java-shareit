package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UserGetDto {
    private long id;
    private String name;
    private String email;
}

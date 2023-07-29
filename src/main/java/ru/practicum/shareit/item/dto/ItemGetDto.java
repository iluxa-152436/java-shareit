package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ItemGetDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
}

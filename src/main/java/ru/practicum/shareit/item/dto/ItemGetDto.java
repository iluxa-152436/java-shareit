package ru.practicum.shareit.item.dto;

import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ItemGetDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
}

package ru.practicum.shareit.item.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ItemDto {
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
}

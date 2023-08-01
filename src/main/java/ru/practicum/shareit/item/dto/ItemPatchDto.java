package ru.practicum.shareit.item.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemPatchDto {
    private String name;
    private String description;
    private Boolean available;
}

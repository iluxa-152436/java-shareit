package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ItemShortDto {
    @NotBlank
    private long id;
    @NotBlank
    private String name;
}

package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Item {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private Boolean available;
    @NotBlank
    private long ownerId;

    public Item(Item item) {
        this(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId());
    }
}

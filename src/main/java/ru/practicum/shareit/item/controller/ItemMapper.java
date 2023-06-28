package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ModelMapper modelMapper;

    public Item toEntity(ItemDto itemDto, long ownerId) {
        Item item = modelMapper.map(itemDto, Item.class);
        item.setOwnerId(ownerId);
        return item;
    }

    public Item toEntity(PatchItemDto patchItemDto, Item oldItem) {
        Item item = new Item(oldItem);
        if (patchItemDto.getAvailable() != null) {
            item.setAvailable(patchItemDto.getAvailable());
        }
        if (patchItemDto.getDescription() != null) {
            item.setDescription(patchItemDto.getDescription());
        }
        if (patchItemDto.getName() != null) {
            item.setName(patchItemDto.getName());
        }
        return item;
    }
}

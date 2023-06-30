package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

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
        Optional.ofNullable(patchItemDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(patchItemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(patchItemDto.getName()).ifPresent(item::setName);
        return item;
    }
}

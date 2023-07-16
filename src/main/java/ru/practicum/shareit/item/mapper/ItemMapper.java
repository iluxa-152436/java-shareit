package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    public static Item toEntity(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUser(user);
        return item;
    }

    public static Item toEntity(ItemPatchDto itemPatchDto, Item oldItem) {
        Item item = new Item(oldItem);
        Optional.ofNullable(itemPatchDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(itemPatchDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemPatchDto.getName()).ifPresent(item::setName);
        return item;
    }

    public static ItemGetDto toItemGetDto(Item item) {
        ItemGetDto itemGetDto = new ItemGetDto();
        itemGetDto.setId(item.getId());
        itemGetDto.setName(item.getName());
        itemGetDto.setDescription(item.getDescription());
        itemGetDto.setAvailable(item.getAvailable());
        itemGetDto.setOwnerId(item.getUser().getId());
        return itemGetDto;
    }

    public static List<ItemGetDto> toItemGetDto(List<Item> items) {
        List<ItemGetDto> itemGetDtoList = new ArrayList<>();
        for (Item item : items) {
            itemGetDtoList.add(toItemGetDto(item));
        }
        return itemGetDtoList;
    }

    public static ItemShortDto toItemShortDto(Item item) {
        return new ItemShortDto(item.getId(), item.getName());
    }
}

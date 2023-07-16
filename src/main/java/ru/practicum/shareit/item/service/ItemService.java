package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemGetDto getItemById(long itemId);

    List<ItemGetDto> getItemsByOwnerId(long ownerId);

    ItemGetDto updateItem(long ownerId, long itemId, ItemPatchDto itemPatchDto);

    List<ItemGetDto> getAvailableItemsByFilter(String text);

    ItemGetDto addNewItem(ItemDto itemDto, long userId);

    Item getFullItemDtoById(long itemId);
}

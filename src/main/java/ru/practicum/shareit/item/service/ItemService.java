package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addNewItem(Item item);

    Item getItemById(long itemId);

    List<Item> getItemsByOwnerId(long ownerId);

    Item updateItem(long ownerId, long itemId, PatchItemDto patchItemDto);

    List<Item> getAvailableItemsByFilter(String text);
}

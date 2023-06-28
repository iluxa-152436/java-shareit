package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item addNewItem(Item item);

    Item getItemById(long itemId);

    List<Item> getItemsByOwnerId(long ownerId);

    Item updateItem(Item newItem, long ownerId);

    List<Item> getAvailableItemsByFilter(String text);
}

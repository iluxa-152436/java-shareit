package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item save(Item item);

    Item findById(long id);

    List<Item> findByOwnerId(long ownerId);

    Item update(Item newItem);

    List<Item> findAvailableByNameOrDescription(String text);
}

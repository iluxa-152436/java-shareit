package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.exception.ItemAccessException;
import ru.practicum.shareit.item.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final UserService userService;
    private final ItemMapper mapper;

    @Override
    public Item addNewItem(Item item) {
        checkUserId(item);
        return storage.save(item);
    }

    @Override
    public Item updateItem(long ownerId, long itemId, PatchItemDto patchItemDto) {
        Item newItem = mapper.toEntity(patchItemDto, getItemById(itemId));
        checkItemOwner(newItem, ownerId);
        checkUserId(newItem);
        return storage.update(newItem);
    }

    @Override
    public Item getItemById(long itemId) {
        return storage.findById(itemId);
    }

    @Override
    public List<Item> getItemsByOwnerId(long ownerId) {
        return storage.findByOwnerId(ownerId);
    }

    @Override
    public List<Item> getAvailableItemsByFilter(String text) {
        return storage.findAvailableByNameOrDescription(text);
    }

    private static void checkItemOwner(Item newItem, long ownerId) {
        if (newItem.getOwnerId() != ownerId) {
            throw new ItemAccessException("Item access error");
        }
    }

    private void checkUserId(Item newItem) {
        if (!userService.isValid(newItem.getOwnerId())) {
            throw new UserDoesNotExistException("User with id " + newItem.getOwnerId() + " doesn't exist");
        }
    }
}

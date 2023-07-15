package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.exception.ItemAccessException;
import ru.practicum.shareit.item.storage.DbItemStorage;
import ru.practicum.shareit.user.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final DbItemStorage storage;
    private final UserService userService;
    private final ItemMapper mapper;

    @Override
    public ItemGetDto addNewItem(ItemDto itemDto, long userId) {
        userService.isValid(userId);
        return mapper.toItemGetDto(storage.save(mapper.toEntity(itemDto, userService.getUserById(userId))));
    }

    @Override
    public ItemGetDto updateItem(long ownerId, long itemId, ItemPatchDto itemPatchDto) {
        Item newItem = mapper.toEntity(itemPatchDto, storage.findById(itemId).orElseThrow());
        checkItemOwner(newItem, ownerId);
        checkUserId(newItem);
        return mapper.toItemGetDto(storage.save(newItem));
    }

    @Override
    public ItemGetDto getItemById(long itemId) {
        return mapper.toItemGetDto(storage.findById(itemId)
                .orElseThrow(()-> new ItemNotFoundException("Item with id " + itemId + " doesn't exist")));
    }

    @Override
    public List<ItemGetDto> getItemsByOwnerId(long ownerId) {
        return mapper.toItemGetDto(storage.findByOwnerId(ownerId));
    }

    @Override
    public List<ItemGetDto> getAvailableItemsByFilter(String text) {
        return mapper.toItemGetDto(storage.findAvailableByNameOrDescription(text));
    }

    private static void checkItemOwner(Item newItem, long ownerId) {
        if (newItem.getUser().getId()!= ownerId) {
            throw new ItemAccessException("Item access error");
        }
    }

    private void checkUserId(Item newItem) {
        if (!userService.isValid(newItem.getUser().getId())) {
            throw new UserDoesNotExistException("User with id " + newItem.getUser().getId()+ " doesn't exist");
        }
    }
}

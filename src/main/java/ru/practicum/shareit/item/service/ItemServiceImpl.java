package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.exception.AccessException;
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

    @Override
    public ItemGetDto addNewItem(ItemDto itemDto, long userId) {
        userService.isValid(userId);
        return ItemMapper.toItemGetDto(storage.save(ItemMapper.toEntity(itemDto, userService.getUserById(userId))));
    }

    @Override
    public Item getFullItemDtoById(long itemId) {
        return storage.findById(itemId)
                .orElseThrow(()-> new ItemNotFoundException("Item with id " + itemId + " doesn't exist"));
    }

    @Override
    public ItemGetDto updateItem(long ownerId, long itemId, ItemPatchDto itemPatchDto) {
        Item newItem = ItemMapper.toEntity(itemPatchDto, storage.findById(itemId).orElseThrow());
        checkItemOwner(newItem, ownerId);
        checkUserId(newItem);
        return ItemMapper.toItemGetDto(storage.save(newItem));
    }

    @Override
    public ItemGetDto getItemById(long itemId) {
        return ItemMapper.toItemGetDto(storage.findById(itemId)
                .orElseThrow(()-> new ItemNotFoundException("Item with id " + itemId + " doesn't exist")));
    }

    @Override
    public List<ItemGetDto> getItemsByOwnerId(long ownerId) {
        return ItemMapper.toItemGetDto(storage.findByOwnerId(ownerId));
    }

    @Override
    public List<ItemGetDto> getAvailableItemsByFilter(String text) {
        return ItemMapper.toItemGetDto(storage.findAvailableByNameOrDescription(text));
    }

    private static void checkItemOwner(Item newItem, long ownerId) {
        if (newItem.getUser().getId()!= ownerId) {
            throw new AccessException("Item access error");
        }
    }

    private void checkUserId(Item newItem) {
        if (!userService.isValid(newItem.getUser().getId())) {
            throw new UserDoesNotExistException("User with id " + newItem.getUser().getId()+ " doesn't exist");
        }
    }
}

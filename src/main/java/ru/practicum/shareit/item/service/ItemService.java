package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemGetDtoFull getItemById(long itemId, long userId);

    List<ItemGetDtoFull> getItemsByOwnerId(long ownerId);

    ItemGetDto updateItem(long ownerId, long itemId, ItemPatchDto itemPatchDto);

    List<ItemGetDto> getAvailableItemsByFilter(String text);

    ItemGetDto addNewItem(ItemDto itemDto, long userId);

    Item getFullItemDtoById(long itemId);

    CommentGetDto addComment(CommentPostDto commentPostDto, long userId, long itemId);
}

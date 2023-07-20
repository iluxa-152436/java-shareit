package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final BookingStorage bookingStorage;
    private final CommentStorage commentStorage;
    private final UserService userService;

    @Transactional
    @Override
    public ItemGetDto addNewItem(ItemDto itemDto, long userId) {
        userService.isValidUser(userId);
        return ItemMapper.toItemGetDto(itemStorage.save(ItemMapper.toEntity(itemDto, userService.getUserById(userId))));
    }

    @Override
    public Item getFullItemDtoById(long itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " doesn't exist"));
    }

    @Transactional
    @Override
    public ItemGetDto updateItem(long ownerId, long itemId, ItemPatchDto itemPatchDto) {
        Item newItem = ItemMapper.toEntity(itemPatchDto, itemStorage.findById(itemId).orElseThrow());
        checkItemOwner(newItem, ownerId);
        checkUserId(newItem.getUser().getId());
        return ItemMapper.toItemGetDto(itemStorage.save(newItem));
    }

    @Override
    public ItemGetDtoFull getItemById(long itemId, long userId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " doesn't exist"));
        if (userId == item.getUser().getId()) { //запрос от владельца Item
            List<Booking> bookings = bookingStorage.findByItemIn(List.of(item));
            return ItemMapper.toItemGetDtoFull(item,
                    bookings,
                    LocalDateTime.now(),
                    commentStorage.findByItemId(itemId));
        } else { //запрос от обычного пользователя
            return ItemMapper.toItemGetDtoFull(item,
                    Collections.EMPTY_LIST,
                    LocalDateTime.now(),
                    commentStorage.findByItemId(itemId));
        }
    }

    @Override
    public List<ItemGetDtoFull> getItemsByOwnerId(long ownerId) {
        List<Item> items = itemStorage.findByUserId(ownerId);
        List<Booking> bookings = bookingStorage.findByItemIn(items);
        List<Comment> comments = commentStorage.findByItemIdIn(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));
        return ItemMapper.toItemGetDtoFull(items, bookings, LocalDateTime.now(), comments);
    }

    @Override
    public List<ItemGetDto> getAvailableItemsByFilter(String text) {
        return ItemMapper.toItemGetDto(itemStorage.findAvailableByNameOrDescription(text));
    }

    @Transactional
    @Override
    public CommentGetDto addComment(CommentPostDto commentPostDto, long userId, long itemId) {
        User user = userService.getUserById(userId);
        checkBooker(userId, itemId);
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " doesn't exist"));
        return CommentMapper.toCommentGetDto(commentStorage.save(CommentMapper.toEntity(commentPostDto, user, item)));
    }

    private void checkBooker(long userId, long itemId) {
        if (bookingStorage.findCountByBookerIdAndItemIdAndState(userId,
                itemId,
                BookingState.APPROVED,
                LocalDateTime.now()) < 1) {
            throw new IllegalArgumentException("No permission to add comment");
        }
    }

    private void checkUserId(long userId) {
        if (!userService.isValidUser(userId)) {
            throw new UserDoesNotExistException("User with id " + userId + " doesn't exist");
        }
    }

    private void checkItemOwner(Item item, long ownerId) {
        if (item.getUser().getId() != ownerId) {
            throw new AccessException("Item access error");
        }
    }
}

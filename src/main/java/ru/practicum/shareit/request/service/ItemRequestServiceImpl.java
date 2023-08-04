package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage storage;
    private final UserService userService;
    private final ItemStorage itemStorage;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestStorage storage, UserService userService, ItemStorage itemStorage) {
        this.storage = storage;
        this.userService = userService;
        this.itemStorage = itemStorage;
    }

    @Override
    public ItemRequestGetDto addNewItemRequest(ItemRequestDto itemRequestDto, long requesterId) {
        userService.checkUser(requesterId);
        return ItemRequestMapper.toItemRequestGetDto(storage.save(ItemRequestMapper.toEntity(itemRequestDto,
                userService.getUserById(requesterId),
                LocalDateTime.now())), Collections.EMPTY_LIST);
    }

    @Override
    public List<ItemRequestGetDto> getItemRequestsByRequesterId(long requesterId) {
        userService.checkUser(requesterId);
        return ItemRequestMapper.toItemRequestGetDto(storage.findByRequesterId(requesterId),
                itemStorage.findByItemRequestRequesterId(requesterId));
    }

    @Override
    public ItemRequestGetDto getItemRequestById(long requestId, long requesterId) {
        userService.checkUser(requesterId);
        return ItemRequestMapper.toItemRequestGetDto(storage.findById(requestId)
                        .orElseThrow(() -> new ItemNotFoundException("Item request with id "
                                + requestId
                                + " doesn't exist")),
                itemStorage.findByItemRequestRequesterId(requestId));
    }

    @Override
    public List<ItemRequestGetDto> getItemRequestsOtherUsers(long requesterId,
                                                             Optional<Integer> from,
                                                             Optional<Integer> size) {
        userService.checkUser(requesterId);
        if (from.isPresent() && size.isPresent()) {
            Pageable pageableWithSorting = getPageable(from.get(), size.get());
            return ItemRequestMapper.toItemRequestGetDto(storage.findByRequesterIdNot(requesterId, pageableWithSorting),
                    itemStorage.findByItemRequestRequesterIdNot(requesterId));
        } else {
            return ItemRequestMapper.toItemRequestGetDto(storage.findByRequesterIdNot(requesterId),
                    itemStorage.findByItemRequestRequesterIdNot(requesterId));
        }
    }

    private static PageRequest getPageable(Integer from, Integer size) {
        if (from < 0 || size < 1) {
            throw new IllegalArgumentException("from and size must be valid");
        }
        return PageRequest.of(from / size, size, Sort.by("created").ascending());
    }
}

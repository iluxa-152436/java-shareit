package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage storage;
    private final UserService userService;

    @Override
    public ItemRequestGetDto addNewItemRequest(ItemRequestDto itemRequestDto, long requesterId) {
        return ItemRequestMapper.toItemRequestGetDto(storage.save(ItemRequestMapper.toEntity(itemRequestDto,
                userService.getUserById(requesterId),
                LocalDateTime.now())), Collections.EMPTY_LIST);
    }

    @Override
    public List<ItemRequestGetDto> getItemRequestsByRequesterId(long requesterId) {
        return null;
    }

    @Override
    public ItemRequestGetDto getItemRequestById(long requestId) {
        return null;
    }

    @Override
    public List<ItemRequestGetDto> getItemRequestsOtherUsers(long requesterId, long from, int size) {
        return null;
    }
}

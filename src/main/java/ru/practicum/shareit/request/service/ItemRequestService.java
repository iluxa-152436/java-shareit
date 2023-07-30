package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;

import java.util.List;
import java.util.Optional;

public interface ItemRequestService {
    ItemRequestGetDto addNewItemRequest(ItemRequestDto itemRequestDto, long requesterId);
    List<ItemRequestGetDto> getItemRequestsByRequesterId(long requesterId);
    ItemRequestGetDto getItemRequestById(long requestId, long requesterId);
    List<ItemRequestGetDto> getItemRequestsOtherUsers(long requesterId, Optional<Integer> from, Optional<Integer> size);
}

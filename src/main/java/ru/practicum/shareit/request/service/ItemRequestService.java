package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestGetDto addNewItemRequest(ItemRequestDto itemRequestDto, long requesterId);
    List<ItemRequestGetDto> getItemRequestsByRequesterId(long requesterId);
    ItemRequestGetDto getItemRequestById(long requestId);
    List<ItemRequestGetDto> getItemRequestsOtherUsers(long requesterId, long from, int size);
}

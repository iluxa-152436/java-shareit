package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest toEntity(ItemRequestDto itemRequestDto, User requester, LocalDateTime created) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequester(requester);
        itemRequest.setCreated(created);
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static ItemRequestGetDto toItemRequestGetDto(ItemRequest itemRequest, List<Item> items) {
        ItemRequestGetDto itemRequestGetDto = new ItemRequestGetDto();
        itemRequestGetDto.setId(itemRequest.getId());
        itemRequestGetDto.setDescription(itemRequest.getDescription());
        itemRequestGetDto.setCreated(itemRequest.getCreated());
        itemRequestGetDto.setItems(ItemMapper.toItemGetDtoWithRequestId(items));
        return itemRequestGetDto;
    }
}

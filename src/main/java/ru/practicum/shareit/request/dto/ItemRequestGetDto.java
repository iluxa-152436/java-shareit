package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemGetDtoWithRequestId;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ItemRequestGetDto {
    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemGetDtoWithRequestId> items;
}

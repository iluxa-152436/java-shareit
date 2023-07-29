package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ItemGetDtoWithRequestId {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;
}

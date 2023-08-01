package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;


@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class ItemGetDtoFull {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentGetDto> comments;
}

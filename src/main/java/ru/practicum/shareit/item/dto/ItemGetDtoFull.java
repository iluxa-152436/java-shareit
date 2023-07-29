package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;


@NoArgsConstructor
@Setter
@Getter
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

package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;


@NoArgsConstructor
@Setter
@Getter
public class ItemGetDtoOwner {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long ownerId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
}

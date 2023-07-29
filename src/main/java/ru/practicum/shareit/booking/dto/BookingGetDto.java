package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.user.dto.UserShortDto;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
public class BookingGetDto {
    private long id;
    private BookingState status;
    private LocalDateTime start;
    private LocalDateTime end;
    private UserShortDto booker;
    private ItemShortDto item;
}

package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class BookingMapper {
    public static Booking toEntity(BookingDto bookingDto, User creator, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setCreator(creator);
        booking.setState(BookingState.WAITING);
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingGetDto toBookingGetDto(Booking booking) {
        BookingGetDto bookingGetDto = new BookingGetDto();
        bookingGetDto.setId(booking.getId());
        bookingGetDto.setStatus(booking.getState());
        bookingGetDto.setItem(ItemMapper.toItemShortDto(booking.getItem()));
        bookingGetDto.setBooker(UserMapper.toShortDto(booking.getCreator()));
        bookingGetDto.setStart(booking.getStart());
        bookingGetDto.setEnd(booking.getEnd());
        return bookingGetDto;
    }
}

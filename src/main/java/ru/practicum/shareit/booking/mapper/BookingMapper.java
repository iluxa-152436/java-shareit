package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {
    public static Booking toEntity(BookingDto bookingDto, User creator, Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(creator);
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
        bookingGetDto.setBooker(UserMapper.toShortDto(booking.getBooker()));
        bookingGetDto.setStart(booking.getStart());
        bookingGetDto.setEnd(booking.getEnd());
        return bookingGetDto;
    }

    public static List<BookingGetDto> toBookingGetDto(List<Booking> bookings) {
        List<BookingGetDto> bookingGetDtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingGetDtoList.add(toBookingGetDto(booking));
        }
        return bookingGetDtoList;
    }

    public static BookingShortDto toBookingShortDto(Booking booking) {
        BookingShortDto bookingShortDto = new BookingShortDto();
        bookingShortDto.setId(booking.getId());
        bookingShortDto.setBookerId(booking.getBooker().getId());
        return bookingShortDto;
    }
}

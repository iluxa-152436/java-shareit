package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;

import java.util.List;

public interface BookingService {
    BookingGetDto getBookingById(long bookingId, long userId);
    BookingGetDto addNewBooking(BookingDto bookingDto, long creatorId);
    List<BookingGetDto> getBookingsByCreatorId(long creatorId, BookingStateFilter stateFilter);
    List<BookingGetDto> getBookingsByItemOwnerId(long ownerId, BookingStateFilter stateFilter);
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    BookingGetDto getBookingById(long bookingId, long userId);

    BookingGetDto addNewBooking(BookingDto bookingDto, long creatorId);

    List<BookingGetDto> getBookingsByBookerId(long creatorId,
                                              BookingStateFilter stateFilter,
                                              Optional<Integer> from,
                                              Optional<Integer> size);

    List<BookingGetDto> getBookingsByItemOwnerId(long ownerId,
                                                 BookingStateFilter stateFilter,
                                                 Optional<Integer> from,
                                                 Optional<Integer> size);

    BookingGetDto updateBookingByItemOwner(long bookingId, long ownerId, boolean isApproved);
}

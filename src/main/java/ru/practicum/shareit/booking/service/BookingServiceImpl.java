package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingStorage storage;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingGetDto getBookingById(long bookingId, long userId) {
        Booking booking = storage.findById(bookingId).orElseThrow();
        if (booking.getCreator().getId() != userId || booking.getItem().getUser().getId() != userId) {
            throw new AccessException("Booking access error");
        }
        return BookingMapper.toBookingGetDto(booking);
    }

    @Override
    public BookingGetDto addNewBooking(BookingDto bookingDto, long creatorId) {
        checkDate(bookingDto.getStart(), bookingDto.getEnd());
        Item item = itemService.getFullItemDtoById(bookingDto.getItemId());
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Item is not available");
        }
        Booking booking = storage.save(BookingMapper.toEntity(bookingDto,
                userService.getUserById(creatorId),
                item));
        return BookingMapper.toBookingGetDto(booking);
    }

    @Override
    public List<BookingGetDto> getBookingsByCreatorId(long creatorId, BookingStateFilter stateFilter) {
        return null;
    }

    @Override
    public List<BookingGetDto> getBookingsByItemOwnerId(long ownerId, BookingStateFilter stateFilter) {
        return null;
    }

    private void checkDate(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }
}

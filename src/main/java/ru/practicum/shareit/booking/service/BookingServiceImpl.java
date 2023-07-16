package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.exception.UserDoesNotExistException;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.mapper.BookingMapper.*;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingStorage storage;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    public BookingGetDto getBookingById(long bookingId, long userId) {
        Booking booking = storage.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + bookingId + " doesn't exist"));
        if (booking.getBooker().getId() != userId && booking.getItem().getUser().getId() != userId) {
            throw new BookingNotFoundException("Booking with id " + bookingId + " doesn't exist");
        }
        return toBookingGetDto(booking);
    }

    @Override
    public BookingGetDto addNewBooking(BookingDto bookingDto, long bookerId) {
        checkDate(bookingDto.getStart(), bookingDto.getEnd());
        Item item = itemService.getFullItemDtoById(bookingDto.getItemId());
        checkBooker(bookerId, item);
        if (!item.getAvailable()) {
            throw new IllegalArgumentException("Item is not available");
        }
        Booking booking = storage.save(toEntity(bookingDto,
                userService.getUserById(bookerId),
                item));
        return toBookingGetDto(booking);
    }

    @Override
    public List<BookingGetDto> getBookingsByBookerId(long bookerId, BookingStateFilter stateFilter) {
        checkUser(bookerId);
        LocalDateTime now = LocalDateTime.now();
        switch (stateFilter) {
            case ALL:
                return toBookingGetDto(storage.findByBookerIdOrderByStartDesc(bookerId));
            case WAITING:
                return toBookingGetDto(storage.findByBookerIdAndStateOrderByStartDesc(bookerId,
                        BookingState.WAITING));
            case REJECTED:
                return toBookingGetDto(storage.findByBookerIdAndStateOrderByStartDesc(bookerId,
                        BookingState.REJECTED));
            case PAST:
                return toBookingGetDto(storage
                        .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now));
            case FUTURE:
                return toBookingGetDto(storage
                        .findByBookerIdAndStartAfterOrderByStartDesc(bookerId, now));
            case CURRENT:
                return toBookingGetDto(storage
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, now, now));
            default:
                return null;
        }
    }

    @Override
    public List<BookingGetDto> getBookingsByItemOwnerId(long ownerId, BookingStateFilter stateFilter) {
        checkUser(ownerId);
        LocalDateTime now = LocalDateTime.now();
        switch (stateFilter) {
            case ALL:
                return toBookingGetDto(storage.findByOwnerIdOrderByStartDesc(ownerId));
            case WAITING:
                return toBookingGetDto(storage
                        .findByOwnerIdAndStateOrderByStartDesc(ownerId, BookingState.WAITING));
            case REJECTED:
                return toBookingGetDto(storage
                        .findByOwnerIdAndStateOrderByStartDesc(ownerId, BookingState.REJECTED));
            case PAST:
                return toBookingGetDto(storage
                        .findByOwnerIdAndStateAndEndBeforeOrderByStartDesc(ownerId, now));
            case FUTURE:
                return toBookingGetDto(storage
                        .findByOwnerIdAndStateAndStartAfterOrderByStartDesc(ownerId, now));
            case CURRENT:
                return toBookingGetDto(storage
                        .findByOwnerIdAndStateAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now));
            default:
                return null;
        }
    }

    @Override
    public BookingGetDto updateBookingByItemOwner(long bookingId, long ownerId, boolean isApproved) {
        Booking booking = storage.findById(bookingId).orElseThrow();
        if (booking.getItem().getUser().getId() != ownerId) {
            throw new BookingNotFoundException("Booking with id " + bookingId + " doesn't exist");
        }
        if (isApproved) {
            if (booking.getState().equals(BookingState.WAITING)) {
                booking.setState(BookingState.APPROVED);
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            booking.setState(BookingState.REJECTED);
        }
        storage.save(booking);
        return toBookingGetDto(booking);
    }

    private void checkDate(LocalDateTime start, LocalDateTime end) {
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    private void checkUser(long userId) {
        if (!userService.isValidUser(userId)) {
            throw new UserDoesNotExistException("User with id " + userId + " doesn't exist");
        }
    }

    private void checkBooker(long bookerId, Item item) {
        if (bookerId == item.getUser().getId()) {
            throw new ItemNotFoundException("Item is not available for booking by the owner");
        }
    }
}

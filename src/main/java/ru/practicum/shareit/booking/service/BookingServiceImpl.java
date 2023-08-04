package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.booking.mapper.BookingMapper.*;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingStorage storage;
    private final UserService userService;
    private final ItemService itemService;

    @Autowired
    public BookingServiceImpl(BookingStorage storage, UserService userService, ItemService itemService) {
        this.storage = storage;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingGetDto getBookingById(long bookingId, long userId) {
        Booking booking = storage.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with id " + bookingId + " doesn't exist"));
        if (booking.getBooker().getId() != userId && booking.getItem().getUser().getId() != userId) {
            throw new BookingNotFoundException("Booking with id " + bookingId + " doesn't exist");
        }
        return toBookingGetDto(booking);
    }

    @Transactional
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
    public List<BookingGetDto> getBookingsByBookerId(long bookerId,
                                                     BookingStateFilter stateFilter,
                                                     Optional<Integer> from,
                                                     Optional<Integer> size) {
        userService.checkUser(bookerId);
        LocalDateTime now = LocalDateTime.now();
        Optional<PageRequest> pageable = getPageable(from, size);
        switch (stateFilter) {
            case ALL:
                return pageable.map(pageRequest -> toBookingGetDto(storage.findByBookerIdOrderByStartDesc(bookerId,
                                pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage.findByBookerIdOrderByStartDesc(bookerId)));
            case WAITING:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByBookerIdAndStateOrderByStartDesc(bookerId, BookingState.WAITING, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByBookerIdAndStateOrderByStartDesc(bookerId, BookingState.WAITING)));
            case REJECTED:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByBookerIdAndStateOrderByStartDesc(bookerId, BookingState.REJECTED, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByBookerIdAndStateOrderByStartDesc(bookerId, BookingState.REJECTED)));
            case PAST:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, now)));
            case FUTURE:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByBookerIdAndStartAfterOrderByStartDesc(bookerId, now, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByBookerIdAndStartAfterOrderByStartDesc(bookerId, now)));
            case CURRENT:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                                        now,
                                        now,
                                        pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, now, now)));
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public List<BookingGetDto> getBookingsByItemOwnerId(long ownerId,
                                                        BookingStateFilter stateFilter,
                                                        Optional<Integer> from,
                                                        Optional<Integer> size) {
        userService.checkUser(ownerId);
        LocalDateTime now = LocalDateTime.now();
        Optional<PageRequest> pageable = getPageable(from, size);
        switch (stateFilter) {
            case ALL:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByItemUserIdOrderByStartDesc(ownerId, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage.findByItemUserIdOrderByStartDesc(ownerId)));
            case WAITING:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByItemUserIdAndStateOrderByStartDesc(ownerId, BookingState.WAITING, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByItemUserIdAndStateOrderByStartDesc(ownerId, BookingState.WAITING)));
            case REJECTED:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByItemUserIdAndStateOrderByStartDesc(ownerId, BookingState.REJECTED, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByItemUserIdAndStateOrderByStartDesc(ownerId, BookingState.REJECTED)));
            case PAST:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByItemUserIdAndEndBeforeOrderByStartDesc(ownerId, now, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByItemUserIdAndEndBeforeOrderByStartDesc(ownerId, now)));
            case FUTURE:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByItemUserIdAndStartAfterOrderByStartDesc(ownerId, now, pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByItemUserIdAndStartAfterOrderByStartDesc(ownerId, now)));
            case CURRENT:
                return pageable.map(pageRequest -> toBookingGetDto(storage
                                .findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId,
                                        now,
                                        now,
                                        pageRequest)))
                        .orElseGet(() -> toBookingGetDto(storage
                                .findByItemUserIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, now, now)));
            default:
                throw new IllegalArgumentException();
        }
    }

    @Transactional
    @Override
    public BookingGetDto updateBookingByItemOwner(long bookingId, long ownerId, boolean isApproved) {
        Booking booking = storage.findById(bookingId).orElseThrow();
        if (booking.getItem().getUser().getId() != ownerId) {
            throw new BookingNotFoundException("Booking with id " + bookingId + " doesn't exist");
        }
        if (isApproved) {
            if (booking.getState() == BookingState.WAITING) {
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

    private void checkBooker(long bookerId, Item item) {
        if (bookerId == item.getUser().getId()) {
            throw new ItemNotFoundException("Item is not available for booking by the owner");
        }
    }

    private static Optional<PageRequest> getPageable(Optional<Integer> from, Optional<Integer> size) {
        if (from.isPresent() && size.isPresent()) {
            if (from.get() < 0 || size.get() < 1) {
                throw new IllegalArgumentException("from and size must be valid");
            }
            return Optional.of(PageRequest.of(from.get() / size.get(), size.get()));
        } else return Optional.empty();
    }
}

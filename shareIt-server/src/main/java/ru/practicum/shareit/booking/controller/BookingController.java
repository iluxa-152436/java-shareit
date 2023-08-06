package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingStateFilter;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.constant.Constant.DEFAULT_STATE_FILTER;
import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingGetDto add(@RequestBody @Valid BookingDto bookingDto,
                             @RequestHeader(HEADER_USER_ID) long bookerId) {
        return bookingService.addNewBooking(bookingDto, bookerId);
    }

    @GetMapping("/{bookingId}")
    public BookingGetDto getById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingGetDto> getBookingsByItemOwnerId(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                        @RequestParam(defaultValue = DEFAULT_STATE_FILTER) BookingStateFilter state,
                                                        @RequestParam Optional<Integer> from,
                                                        @RequestParam Optional<Integer> size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, ownerId, from, size);
        return bookingService.getBookingsByItemOwnerId(ownerId, state, from, size);
    }

    @GetMapping
    public List<BookingGetDto> getBookingsByBookerId(@RequestHeader(HEADER_USER_ID) long bookerId,
                                                     @RequestParam(defaultValue = DEFAULT_STATE_FILTER) BookingStateFilter state,
                                                     @RequestParam Optional<Integer> from,
                                                     @RequestParam Optional<Integer> size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, bookerId, from, size);
        return bookingService.getBookingsByBookerId(bookerId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto patchBookingByItemOwner(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                 @PathVariable long bookingId,
                                                 @RequestParam("approved") boolean isApproved) {
        return bookingService.updateBookingByItemOwner(bookingId, ownerId, isApproved);
    }
}

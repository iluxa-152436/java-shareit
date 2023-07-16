package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingStateFilter;

import javax.validation.Valid;

import java.util.List;

import static ru.practicum.shareit.constant.Constant.DEFAULT_STATE_FILTER;
import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingGetDto add(@RequestBody @Valid BookingDto bookingDto,
                             @RequestHeader(HEADER_USER_ID) long creatorId) {
        return bookingService.addNewBooking(bookingDto, creatorId);
    }

    @GetMapping("/{bookingId}")
    public BookingGetDto getById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingGetDto> getBookingsByItemOwnerId(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                        @RequestParam(defaultValue = DEFAULT_STATE_FILTER) BookingStateFilter state) {
        return bookingService.getBookingsByItemOwnerId(ownerId, state);
    }

    @GetMapping
    public List<BookingGetDto> getBookingsByBookerId(@RequestHeader(HEADER_USER_ID) long creatorId,
                                                     @RequestParam(defaultValue = DEFAULT_STATE_FILTER) BookingStateFilter state) {
        return bookingService.getBookingsByBookerId(creatorId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingGetDto patchBookingByItemOwner(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                 @PathVariable long bookingId,
                                                 @RequestParam("approved") boolean isApproved) {
        return bookingService.updateBookingByItemOwner(bookingId, ownerId, isApproved);
    }
}
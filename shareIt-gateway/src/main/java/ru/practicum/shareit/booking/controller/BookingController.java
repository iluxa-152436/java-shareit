package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;

import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid BookingDto bookingDto,
                                      @RequestHeader(HEADER_USER_ID) long bookerId) {
        return client.addNewBooking(bookingDto, bookerId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long bookingId) {
        return client.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByItemOwnerId(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                           @RequestParam(defaultValue = "ALL") String state,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, ownerId, from, size);
        return client.getBookingsByItemOwnerId(ownerId, state, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBookerId(@RequestHeader(HEADER_USER_ID) long bookerId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, bookerId, from, size);
        return client.getBookingsByBookerId(bookerId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> patchBookingByItemOwner(@RequestHeader(HEADER_USER_ID) long ownerId,
                                                          @PathVariable long bookingId,
                                                          @RequestParam("approved") boolean isApproved) {
        return client.updateBookingByItemOwner(bookingId, ownerId, isApproved);
    }
}

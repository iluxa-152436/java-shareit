package ru.practicum.shareit.booking.service;

public enum BookingStateFilter {
    ALL("all"),
    CURRENT("current"),
    PAST("past"),
    FUTURE("future"),
    WAITING("waiting"),
    REJECTED("rejected");

    private final String title;

    BookingStateFilter(String title) {
        this.title = title;
    }
}

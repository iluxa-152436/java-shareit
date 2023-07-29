package ru.practicum.shareit.booking.entity;

public enum BookingState {
    WAITING("waiting"),
    APPROVED("approved"),
    REJECTED("rejected");

    private final String title;

    BookingState(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.entity.Booking;

public interface BookingStorage extends JpaRepository<Booking, Long> {
}

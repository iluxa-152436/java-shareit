package ru.practicum.shareit.booking.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class BookingStateConverter implements AttributeConverter<BookingState, String> {
    @Override
    public String convertToDatabaseColumn(BookingState bookingState) {
        if (bookingState == null) {
            return null;
        }
        return bookingState.getTitle();
    }

    @Override
    public BookingState convertToEntityAttribute(String title) {
        if (title == null) {
            return null;
        }
        return Stream.of(BookingState.values())
                .filter(c -> c.getTitle().equals(title))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class BookingDto {
    @NotNull
    private long itemId;
    @NotNull(message = "Start date cannot be null")
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @FutureOrPresent(message = "Start date must be in future or present")
    private LocalDateTime start;
    @NotNull(message = "End date cannot be null")
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @FutureOrPresent(message = "End date must be in future or present")
    private LocalDateTime end;
}

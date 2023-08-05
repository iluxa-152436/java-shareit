package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingStateFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mvc;
    private LocalDateTime now;
    private BookingDto bookingDto;
    private BookingGetDto bookingGetDto;

    @BeforeEach
    void prepare() {
        now = LocalDateTime.now();
        bookingDto = prepareBookingDto(now.plusDays(1));
        bookingGetDto = prepareBookingGetDto(now.plusDays(1));
    }

    @Test
    void addTest() throws Exception {
        when(service.addNewBooking(any(BookingDto.class), anyLong())).thenReturn(bookingGetDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingGetDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingGetDto.getStatus().name()))
                .andExpect(jsonPath("$.item.id").value(bookingGetDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingGetDto.getItem().getName()));
    }

    @Test
    void getByIdTest() throws Exception {
        when(service.getBookingById(anyLong(), anyLong())).thenReturn(bookingGetDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingGetDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingGetDto.getStatus().name()))
                .andExpect(jsonPath("$.item.id").value(bookingGetDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingGetDto.getItem().getName()));
    }

    @Test
    void getBookingsByItemOwnerIdTest() throws Exception {
        when(service.getBookingsByItemOwnerId(anyLong(),
                any(BookingStateFilter.class),
                any(Optional.class),
                any(Optional.class))).thenReturn(List.of(bookingGetDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(bookingGetDto.getId()))
                .andExpect(jsonPath("$.[0].status").value(bookingGetDto.getStatus().name()))
                .andExpect(jsonPath("$.[0].item.id").value(bookingGetDto.getItem().getId()))
                .andExpect(jsonPath("$.[0]item.name").value(bookingGetDto.getItem().getName()));
    }

    @Test
    void getBookingsByBookerIdTest() throws Exception {
        when(service.getBookingsByBookerId(anyLong(),
                any(BookingStateFilter.class),
                any(Optional.class),
                any(Optional.class))).thenReturn(List.of(bookingGetDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(bookingGetDto.getId()))
                .andExpect(jsonPath("$.[0].status").value(bookingGetDto.getStatus().name()))
                .andExpect(jsonPath("$.[0].item.id").value(bookingGetDto.getItem().getId()))
                .andExpect(jsonPath("$.[0]item.name").value(bookingGetDto.getItem().getName()));
    }

    @Test
    void patchBookingByItemOwnerTest() throws Exception {
        when(service.updateBookingByItemOwner(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingGetDto);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingGetDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingGetDto.getStatus().name()))
                .andExpect(jsonPath("$.item.id").value(bookingGetDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingGetDto.getItem().getName()));
    }
}
package ru.practicum.shareit.exception.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.ConflictException;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ExceptionHandlerController.class,
        UserController.class,
        ItemController.class,
        BookingController.class})
class ExceptionHandlerControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private BookingService bookingService;

    @Test
    void handleExceptionTest() throws Exception {
        when(userService.addNewUser(any(UserDto.class))).thenThrow(IllegalArgumentException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(prepareUser(1L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleConflictExceptionTest() throws Exception {
        when(userService.addNewUser(any(UserDto.class))).thenThrow(ConflictException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(prepareUser(1L)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void getTestNotFoundTest() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(UserNotFoundException.class);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchTestAccessExceptionTest() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemPatchDto.class))).thenThrow(AccessException.class);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(prepareItemPatchDto()))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /*@Test
    void getBookingsByBookerIdTest() throws Exception {
        when(bookingService.getBookingsByBookerId(anyLong(),
                any(BookingStateFilter.class),
                any(Optional.class),
                any(Optional.class))).thenThrow(ConversionFailedException.class);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "1")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }*/
}
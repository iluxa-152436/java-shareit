package ru.practicum.shareit.exception.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.AccessException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ExceptionHandlerController.class, UserController.class, ItemController.class})
class ExceptionHandlerControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    UserService userService;
    @MockBean
    ItemService itemService;

    @Test
    void handleException() throws Exception {
        when(userService.addNewUser(any(User.class))).thenThrow(IllegalArgumentException.class);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(prepareUser(1l)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getTestNotFound() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(UserNotFoundException.class);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchTestAccessException() throws Exception {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemPatchDto.class))).thenThrow(AccessException.class);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(prepareItemPatchDto()))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
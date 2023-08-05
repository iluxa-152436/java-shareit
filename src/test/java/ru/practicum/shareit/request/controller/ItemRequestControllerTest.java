package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestService itemRequestService;
    private ItemRequestGetDto itemRequestGetDto;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void prepare() {
        LocalDateTime now = LocalDateTime.now();
        itemRequestDto = prepareItemRequestDto();
        itemRequestGetDto = prepareItemRequestGetDto(now);
    }

    @Test
    void addTest() throws Exception {
        when(itemRequestService.addNewItemRequest(any(ItemRequestDto.class), anyLong())).thenReturn(itemRequestGetDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestGetDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestGetDto.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void getItemRequestByIdTest() throws Exception {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(itemRequestGetDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestGetDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestGetDto.getDescription()))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void getAllItemRequestsByRequesterIdTest() throws Exception {
        when(itemRequestService.getItemRequestsByRequesterId(anyLong())).thenReturn(List.of(itemRequestGetDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(itemRequestGetDto.getId()))
                .andExpect(jsonPath("$.[0].description").value(itemRequestGetDto.getDescription()))
                .andExpect(jsonPath("$.[0].created").exists())
                .andExpect(jsonPath("$.[0].items").exists());
    }

    @Test
    void getAllItemRequestsOtherUsersTest() throws Exception {
        when(itemRequestService.getItemRequestsOtherUsers(anyLong(),
                any(Optional.class),
                any(Optional.class))).thenReturn(List.of(itemRequestGetDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("from", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(itemRequestGetDto.getId()))
                .andExpect(jsonPath("$.[0].description").value(itemRequestGetDto.getDescription()))
                .andExpect(jsonPath("$.[0].created").exists())
                .andExpect(jsonPath("$.[0].items").exists());
    }
}
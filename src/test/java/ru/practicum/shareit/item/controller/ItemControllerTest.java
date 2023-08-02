package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    ItemService service;
    private ItemGetDtoFull itemGetDtoFull;
    private ItemGetDtoWithRequestId itemGetDtoWithRequestId;
    private ItemGetDto itemGetDto;
    private CommentGetDto commentGetDto;
    private CommentPostDto commentPostDto;
    private ItemPatchDto itemPatchDto;
    private ItemDto itemDto;

    @BeforeEach
    void prepare() {
        LocalDateTime now = LocalDateTime.now();
        itemGetDtoFull = prepareItemGetDtoFull();
        itemDto = prepareItemDto();
        itemGetDto = prepareItemGetDto();
        itemGetDtoWithRequestId = prepareItemGetDtoWithRequestId();
        itemPatchDto = prepareItemPatchDto();
        commentGetDto = prepareCommentGetDto(now);
        commentPostDto = prepareCommentPostDto();
    }

    @Test
    void add() throws Exception {
        when(service.addNewItem(any(ItemDto.class), anyLong())).thenReturn(itemGetDtoWithRequestId);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemGetDtoWithRequestId.getId()))
                .andExpect(jsonPath("$.name").value(itemGetDtoWithRequestId.getName()))
                .andExpect(jsonPath("$.description").value(itemGetDtoWithRequestId.getDescription()))
                .andExpect(jsonPath("$.available").value(itemGetDtoWithRequestId.getAvailable()));
    }

    @Test
    void getByOwnerId() throws Exception {
        when(service.getItemsByOwnerId(anyLong())).thenReturn(List.of(itemGetDtoFull));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(itemGetDtoFull.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemGetDtoFull.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemGetDtoFull.getDescription()))
                .andExpect(jsonPath("$.[0].available").value(itemGetDtoFull.getAvailable()));
    }

    @Test
    void getById() throws Exception {
        when(service.getItemById(anyLong(), anyLong())).thenReturn(itemGetDtoFull);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemGetDtoFull.getId()))
                .andExpect(jsonPath("$.name").value(itemGetDtoFull.getName()))
                .andExpect(jsonPath("$.description").value(itemGetDtoFull.getDescription()))
                .andExpect(jsonPath("$.available").value(itemGetDtoFull.getAvailable()));
    }

    @Test
    void patchTest() throws Exception {
        when(service.updateItem(anyLong(), anyLong(), any(ItemPatchDto.class))).thenReturn(itemGetDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemPatchDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemGetDto.getId()))
                .andExpect(jsonPath("$.name").value(itemGetDto.getName()))
                .andExpect(jsonPath("$.description").value(itemGetDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemGetDto.getAvailable()));
    }

    @Test
    void search() throws Exception {
        when(service.getAvailableItemsByFilter(anyString())).thenReturn(List.of(itemGetDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(itemGetDto.getId()))
                .andExpect(jsonPath("$.[0].name").value(itemGetDto.getName()))
                .andExpect(jsonPath("$.[0].description").value(itemGetDto.getDescription()))
                .andExpect(jsonPath("$.[0].available").value(itemGetDto.getAvailable()));
    }

    @Test
    void addComment() throws Exception {
        when(service.addComment(any(CommentPostDto.class), anyLong(), anyLong())).thenReturn(commentGetDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentPostDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentGetDto.getId()))
                .andExpect(jsonPath("$.text").value(commentGetDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentGetDto.getAuthorName()));
    }
}
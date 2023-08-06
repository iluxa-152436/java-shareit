package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentPostDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;

import javax.validation.Valid;

import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid ItemDto itemDto, @RequestHeader(HEADER_USER_ID) long userId) {
        return client.postItem(itemDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByOwnerId(@RequestHeader(HEADER_USER_ID) long userId) {
        return client.getItemsByOwnerId(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId) {
        return client.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> patch(@RequestHeader(HEADER_USER_ID) long userId,
                            @RequestBody ItemPatchDto itemPatchDto,
                            @PathVariable long itemId) {
        return client.patchItem(userId, itemId, itemPatchDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam("text") String text) {
        return client.getAvailableItemsByFilter(text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(HEADER_USER_ID) long userId,
                                    @RequestBody @Valid CommentPostDto commentPostDto,
                                    @PathVariable long itemId) {
        return client.postComment(commentPostDto, userId, itemId);
    }
}

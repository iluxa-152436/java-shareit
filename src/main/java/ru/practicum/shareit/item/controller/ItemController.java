package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemGetDto add(@RequestBody @Valid ItemDto itemDto, @RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.addNewItem(itemDto, userId);
    }

    @GetMapping
    public List<ItemGetDtoFull> getByOwnerId(@RequestHeader(HEADER_USER_ID) long userId) {
        return itemService.getItemsByOwnerId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemGetDtoFull getById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId) {
        return itemService.getItemById(itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemGetDto patch(@RequestHeader(HEADER_USER_ID) long userId,
                            @RequestBody ItemPatchDto itemPatchDto,
                            @PathVariable long itemId) {
        return itemService.updateItem(userId, itemId, itemPatchDto);
    }

    @GetMapping("/search")
    public List<ItemGetDto> search(@RequestParam("text") String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.EMPTY_LIST;
        }
        return itemService.getAvailableItemsByFilter(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentGetDto addComment(@RequestHeader(HEADER_USER_ID) long userId,
                                    @RequestBody @Valid CommentPostDto commentPostDto,
                                    @PathVariable long itemId) {
        return itemService.addComment(commentPostDto, userId, itemId);
    }
}

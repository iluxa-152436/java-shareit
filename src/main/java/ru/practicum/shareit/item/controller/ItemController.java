package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private static final String USERID = "X-Sharer-User-Id";


    @PostMapping
    public ItemGetDto add(@RequestBody @Valid ItemDto itemDto, @RequestHeader(USERID) long userId) {
        return itemService.addNewItem(itemDto, userId);
    }

    @GetMapping
    public List<ItemGetDto> getByOwnerId(@RequestHeader(USERID) long userId) {
        return itemService.getItemsByOwnerId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemGetDto getById(@RequestHeader(USERID) long userId, @PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemGetDto patch(@RequestHeader(USERID) long userId,
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
}

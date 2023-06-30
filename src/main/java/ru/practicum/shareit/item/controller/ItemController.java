package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper mapper;
    private static final String USERID = "X-Sharer-User-Id";


    @PostMapping
    public Item add(@RequestBody @Valid ItemDto itemDto, @RequestHeader(USERID) long userId) {
        Item item = mapper.toEntity(itemDto, userId);
        return itemService.addNewItem(item);
    }

    @GetMapping
    public List<Item> getByOwnerId(@RequestHeader(USERID) long userId) {
        return itemService.getItemsByOwnerId(userId);
    }

    @GetMapping("/{itemId}")
    public Item getById(@RequestHeader(USERID) long userId, @PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public Item patch(@RequestHeader(USERID) long userId,
                      @RequestBody PatchItemDto patchItemDto,
                      @PathVariable long itemId) {
        return itemService.updateItem(userId, itemId, patchItemDto);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam("text") String text) {
        if (StringUtils.isBlank(text)) {
            return Collections.EMPTY_LIST;
        }
        return itemService.getAvailableItemsByFilter(text);
    }
}

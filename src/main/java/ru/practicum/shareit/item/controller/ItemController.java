package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ApiErrorMessage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.PatchItemDto;
import ru.practicum.shareit.item.exception.ItemAccessException;
import ru.practicum.shareit.item.exception.UserDoesNotExistException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper mapper;


    @PostMapping
    public Item add(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = mapper.toEntity(itemDto, userId);
        return itemService.addNewItem(item);
    }

    @GetMapping
    public List<Item> getByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByOwnerId(userId);
    }

    @GetMapping("/{itemId}")
    public Item getById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public Item patch(@RequestHeader("X-Sharer-User-Id") long userId,
                      @RequestBody PatchItemDto patchItemDto,
                      @PathVariable long itemId) {
        Item oldItem = itemService.getItemById(itemId);
        Item newItem = mapper.toEntity(patchItemDto, oldItem);
        return itemService.updateItem(newItem, userId);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam("text") String text) {
        if (text.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return itemService.getAvailableItemsByFilter(text);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ApiErrorMessage> handleException(Exception exception) {
        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorMessage> handleExceptionDefaultResponse(MethodArgumentNotValidException exception) {
        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorMessage(exception.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(value = {UserDoesNotExistException.class})
    public ResponseEntity<ApiErrorMessage> handleUserDoesNotExistResponse(Exception exception) {
        log.debug("Получен код 404 Not found {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {ItemAccessException.class})
    public ResponseEntity<ApiErrorMessage> handleItemAccessException(Exception exception) {
        log.debug("Получен код 403 Forbidden {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiErrorMessage(exception.getMessage()));
    }

}

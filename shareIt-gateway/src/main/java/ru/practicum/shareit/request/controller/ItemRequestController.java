package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.Optional;

import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                      @RequestHeader(HEADER_USER_ID) long requesterId) {
        return client.postItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(HEADER_USER_ID) long requesterId,
                                                @PathVariable long requestId) {
        return client.getItemRequestById(requestId, requesterId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsByRequesterId(@RequestHeader(HEADER_USER_ID) long requesterId) {
        return client.getItemRequestsByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllItemRequestsOtherUsers(@RequestHeader(HEADER_USER_ID) long requesterId,
                                                                @RequestParam Optional<Integer> from,
                                                                @RequestParam Optional<Integer> size) {
        return client.getItemRequestsOtherUsers(requesterId, from, size);
    }
}

package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import static ru.practicum.shareit.constant.Constant.HEADER_USER_ID;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestGetDto add(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader(HEADER_USER_ID) long requesterId) {
        return itemRequestService.addNewItemRequest(itemRequestDto, requesterId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestGetDto getItemRequestById(@RequestHeader(HEADER_USER_ID) long requesterId,
                                                @PathVariable long requestId) {
        return itemRequestService.getItemRequestById(requestId, requesterId);
    }

    @GetMapping
    public List<ItemRequestGetDto> getAllItemRequestsByRequesterId(@RequestHeader(HEADER_USER_ID) long requesterId) {
        return itemRequestService.getItemRequestsByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestGetDto> getAllItemRequestsOtherUsers(@RequestHeader(HEADER_USER_ID) long requesterId,
                                                                @RequestParam Optional<Integer> from,
                                                                @RequestParam Optional<Integer> size) {
        return itemRequestService.getItemRequestsOtherUsers(requesterId, from, size);
    }
}

package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
    @Mock
    ItemRequestStorage storage;
    @Mock
    UserService userService;
    @Mock
    ItemStorage itemStorage;
    @InjectMocks
    ItemRequestServiceImpl service;

    @Test
    void addNewItemRequest() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequestDto itemRequestDto = prepareItemRequestDto();
        ItemRequestGetDto required = prepareItemRequestGetDto(now);
        ItemRequest itemRequest = prepareItemRequest(now);
        doNothing().when(userService).checkUser(anyLong());
        when(storage.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(userService.getUserById(anyLong())).thenReturn(mock(User.class));

        ItemRequestGetDto itemRequestGetDto = service.addNewItemRequest(itemRequestDto, 1L);

        assertEquals(required, itemRequestGetDto);
        verify(storage, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void getItemRequestsByRequesterId() {
        LocalDateTime now = LocalDateTime.now();
        List<ItemRequestGetDto> required = List.of(prepareItemRequestGetDto(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByRequesterId(anyLong())).thenReturn(List.of(prepareItemRequest(now)));
        when(itemStorage.findByItemRequestRequesterId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<ItemRequestGetDto> itemRequestGetDtoList = service.getItemRequestsByRequesterId(2L);

        assertEquals(required, itemRequestGetDtoList);
    }

    @Test
    void getItemRequestById() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = prepareItemRequest(now);
        ItemRequestGetDto required = prepareItemRequestGetDto(now);
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemStorage.findByItemRequestRequesterId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        ItemRequestGetDto itemRequestGetDto = service.getItemRequestById(1L, 1L);

        assertEquals(required, itemRequestGetDto);
    }

    @Test
    void getItemRequestByIdNotFound() {
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.getItemRequestById(1L, 1L));
    }

    @Test
    void getItemRequestsOtherUsersPaginated() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = prepareItemRequest(now);
        List<ItemRequestGetDto> required = List.of(prepareItemRequestGetDto(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByRequesterIdNot(anyLong(), any(Pageable.class))).thenReturn(List.of(itemRequest));
        when(itemStorage.findByItemRequestRequesterIdNot(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<ItemRequestGetDto> itemRequestGetDtoList = service.getItemRequestsOtherUsers(2L,
                Optional.of(1),
                Optional.of(1));

        assertEquals(required, itemRequestGetDtoList);
    }

    @Test
    void getItemRequestsOtherUsers() {
        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = prepareItemRequest(now);
        List<ItemRequestGetDto> required = List.of(prepareItemRequestGetDto(now));
        doNothing().when(userService).checkUser(anyLong());
        when(storage.findByRequesterIdNot(anyLong())).thenReturn(List.of(itemRequest));
        when(itemStorage.findByItemRequestRequesterIdNot(anyLong())).thenReturn(Collections.EMPTY_LIST);

        List<ItemRequestGetDto> itemRequestGetDtoList = service.getItemRequestsOtherUsers(2L,
                Optional.empty(),
                Optional.empty());

        assertEquals(required, itemRequestGetDtoList);
    }
}
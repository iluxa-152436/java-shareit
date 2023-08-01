package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.TestData.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @InjectMocks
    private ItemServiceImpl service;
    @Mock
    private ItemStorage itemStorage;
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private CommentStorage commentStorage;
    @Mock
    private UserService userService;
    @Mock
    private ItemRequestStorage itemRequestStorage;

    @Test
    void addNewItem() {
        ItemDto itemDto = prepareItemDto();
        ItemGetDtoWithRequestId itemGetDtoWithRequestId = prepareItemGetDtoWithRequestId();
        Item item = prepareItem();
        doNothing().when(userService).checkUser(anyLong());
        when(itemStorage.save(any(Item.class))).thenReturn(item);
        when(userService.getUserById(anyLong())).thenReturn(prepareUser(1L));
        when(itemRequestStorage.findById(anyLong())).thenReturn(Optional.empty());

        ItemGetDtoWithRequestId savedItemGetDtoWithRequestId = service.addNewItem(itemDto, item.getUser().getId());

        assertEquals(itemGetDtoWithRequestId, savedItemGetDtoWithRequestId);
        verify(itemStorage, times(1)).save(any(Item.class));
    }

    @Test
    void getFullItemDtoById() {
        Item item = prepareItem();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));

        Item received = service.getFullItemDtoById(1L);

        assertEquals(item, received);
    }

    @Test
    void updateItem() {
        ItemPatchDto itemPatchDto = prepareItemPatchDto();
        Item item = prepareItem();
        Item savedItem = prepareItem();
        savedItem.setDescription(itemPatchDto.getDescription());
        savedItem.setName(itemPatchDto.getName());
        ItemGetDto required = prepareItemGetDto();
        required.setName(itemPatchDto.getName());
        required.setDescription(itemPatchDto.getDescription());
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        doNothing().when(userService).checkUser(anyLong());
        when(itemStorage.save(any(Item.class))).thenReturn(savedItem);

        ItemGetDto itemGetDto = service.updateItem(1L, 1L, itemPatchDto);

        assertEquals(required, itemGetDto);
        verify(itemStorage, times(1)).save(any(Item.class));
    }

    @Test
    void getItemsByOwnerId() {
        ItemGetDtoFull itemGetDtoFull1 = prepareItemGetDtoFull();
        ItemGetDtoFull itemGetDtoFull2 = prepareItemGetDtoFull();
        itemGetDtoFull2.setId(2L);
        List<ItemGetDtoFull> required = List.of(itemGetDtoFull1, itemGetDtoFull2);
        Item item1 = prepareItem();
        Item item2 = prepareItem();
        item2.setId(2L);
        when(itemStorage.findByUserId(1L)).thenReturn(List.of(item1, item2));
        when(bookingStorage.findByItemIn(anyList())).thenReturn(Collections.EMPTY_LIST);
        when(commentStorage.findByItemIdIn(anyList())).thenReturn(Collections.EMPTY_LIST);

        List<ItemGetDtoFull> itemGetDtoFullList = service.getItemsByOwnerId(1L);

        assertEquals(required, itemGetDtoFullList);
    }

    @Test
    void getItemByIdRequestOwner() {
        Item item = prepareItem();
        ItemGetDtoFull itemGetDtoFull = prepareItemGetDtoFull();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingStorage.findByItemIn(anyList())).thenReturn(Collections.EMPTY_LIST);
        when(commentStorage.findByItemId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        ItemGetDtoFull received = service.getItemById(1L, 1L);

        assertEquals(itemGetDtoFull, received);
    }

    @Test
    void getItemByIdRequestOtherUser() {
        Item item = prepareItem();
        ItemGetDtoFull itemGetDtoFull = prepareItemGetDtoFull();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentStorage.findByItemId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        ItemGetDtoFull received = service.getItemById(1L, 2L);

        assertEquals(itemGetDtoFull, received);
    }

    @Test
    void getItemsByOwnerId2() {
        Item item = prepareItem();
        List<Item> itemList = List.of(item);
        ItemGetDtoFull itemGetDtoFull = prepareItemGetDtoFull();
        List<ItemGetDtoFull> required = List.of(itemGetDtoFull);
        when(itemStorage.findByUserId(anyLong())).thenReturn(itemList);
        when(bookingStorage.findByItemIn(anyList())).thenReturn(Collections.EMPTY_LIST);
        when(commentStorage.findByItemIdIn(anyList())).thenReturn(Collections.EMPTY_LIST);

        List<ItemGetDtoFull> items = service.getItemsByOwnerId(1L);

        assertEquals(required, items);
    }

    @Test
    void getAvailableItemsByFilter() {
        List<ItemGetDto> required = List.of(prepareItemGetDto());
        List<Item> itemList = List.of(prepareItem());
        when(itemStorage.findAvailableByNameOrDescription(anyString())).thenReturn(itemList);

        List<ItemGetDto> items = service.getAvailableItemsByFilter("text");

        assertEquals(required, items);
    }

    @Test
    void addComment() {
        LocalDateTime now = LocalDateTime.now();
        Comment comment = prepareComment(now);
        CommentPostDto commentPostDto = prepareCommentPostDto();
        CommentGetDto required = prepareCommentGetDto(now);
        Item item = prepareItem();
        when(bookingStorage.findCountByBookerIdAndItemIdAndState(anyLong(),
                anyLong(),
                any(BookingState.class),
                any(LocalDateTime.class))).thenReturn(1);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentStorage.save(any(Comment.class))).thenReturn(comment);

        CommentGetDto commentGetDto = service.addComment(commentPostDto, 2L, item.getId());

        assertEquals(required, commentGetDto);
        verify(commentStorage, times(1)).save(any(Comment.class));
    }
}
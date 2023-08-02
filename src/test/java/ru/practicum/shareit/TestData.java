package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetDto;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestGetDto;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.dto.UserShortDto;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.Collections;

public class TestData {
    public static User prepareUser(long id) {
        return User.builder()
                .id(id)
                .email("email" + id + "@email.ru")
                .name("name")
                .build();
    }

    public static UserPatchDto prepareUserPatchDto(long id) {
        return new UserPatchDto("name", "email@email.ru");
    }

    public static UserDto prepareUserDto(long id) {
        return new UserDto("name", "email@email.ru");
    }

    public static Item prepareItem() {
        return Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .user(prepareUser(1))
                .build();
    }

    public static ItemDto prepareItemDto() {
        return ItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    public static ItemGetDtoWithRequestId prepareItemGetDtoWithRequestId() {
        return ItemGetDtoWithRequestId.builder()
                .id(1L)
                .available(true)
                .description("description")
                .name("name")
                .build();
    }

    public static ItemGetDtoFull prepareItemGetDtoFull() {
        return ItemGetDtoFull.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .ownerId(1L)
                .comments(Collections.EMPTY_LIST)
                .build();
    }

    public static CommentPostDto prepareCommentPostDto() {
        return new CommentPostDto("comment");
    }

    public static Comment prepareComment(LocalDateTime created) {
        return Comment.builder()
                .text("comment")
                .id(1L)
                .created(created)
                .user(prepareUser(2))
                .build();
    }

    public static CommentGetDto prepareCommentGetDto(LocalDateTime created) {
        return CommentGetDto.builder()
                .created(created)
                .authorName(prepareUser(2L).getName())
                .id(1L)
                .text("comment")
                .build();
    }

    public static ItemGetDto prepareItemGetDto() {
        return ItemGetDto.builder()
                .id(1L)
                .available(true)
                .description("description")
                .name("name")
                .build();
    }

    public static ItemPatchDto prepareItemPatchDto() {
        return ItemPatchDto.builder()
                .available(true)
                .description("updated")
                .name("updated")
                .build();
    }

    public static ItemRequestDto prepareItemRequestDto() {
        return new ItemRequestDto("description");
    }

    public static ItemRequestGetDto prepareItemRequestGetDto(LocalDateTime dateTime) {
        return ItemRequestGetDto.builder()
                .id(1L)
                .description("description")
                .created(dateTime)
                .items(Collections.EMPTY_LIST)
                .build();
    }

    public static ItemRequest prepareItemRequest(LocalDateTime dateTime) {
        return ItemRequest.builder()
                .description("description")
                .id(1L)
                .requester(prepareUser(2L))
                .created(dateTime)
                .build();
    }

    public static UserShortDto prepareUserShortDto(Long id) {
        return new UserShortDto(id);
    }

    public static ItemShortDto prepareItemShortDto(Long id) {
        return new ItemShortDto(id, "name");
    }

    public static BookingGetDto prepareBookingGetDto(LocalDateTime startDateTime) {
        return BookingGetDto.builder()
                .id(1L)
                .booker(prepareUserShortDto(2L))
                .status(BookingState.WAITING)
                .item(prepareItemShortDto(1L))
                .start(startDateTime)
                .end(startDateTime.plusDays(1))
                .build();
    }

    public static Booking prepareBooking(LocalDateTime startDateTime) {
        return Booking.builder()
                .id(1L)
                .booker(prepareUser(2L))
                .state(BookingState.WAITING)
                .item(prepareItem())
                .start(startDateTime)
                .end(startDateTime.plusDays(1))
                .build();
    }

    public static BookingDto prepareBookingDto(LocalDateTime startDateTime) {
        return BookingDto.builder()
                .start(startDateTime)
                .end(startDateTime.plusDays(1))
                .itemId(1L)
                .build();
    }
}

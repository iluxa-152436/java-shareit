package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Item toEntity(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setUser(user);
        return item;
    }

    public static Item toEntity(ItemPatchDto itemPatchDto, Item oldItem) {
        Item item = new Item(oldItem);
        Optional.ofNullable(itemPatchDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(itemPatchDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemPatchDto.getName()).ifPresent(item::setName);
        return item;
    }

    public static ItemGetDto toItemGetDto(Item item) {
        ItemGetDto itemGetDto = new ItemGetDto();
        itemGetDto.setId(item.getId());
        itemGetDto.setName(item.getName());
        itemGetDto.setDescription(item.getDescription());
        itemGetDto.setAvailable(item.getAvailable());
        itemGetDto.setOwnerId(item.getUser().getId());
        return itemGetDto;
    }

    public static List<ItemGetDto> toItemGetDto(List<Item> items) {
        List<ItemGetDto> itemGetDtoList = new ArrayList<>();
        for (Item item : items) {
            itemGetDtoList.add(toItemGetDto(item));
        }
        return itemGetDtoList;
    }

    public static ItemShortDto toItemShortDto(Item item) {
        return new ItemShortDto(item.getId(), item.getName());
    }

    public static List<ItemGetDtoFull> toItemGetDtoFull(List<Item> items,
                                                        List<Booking> bookings,
                                                        LocalDateTime date,
                                                        List<Comment> comments) {
        List<ItemGetDtoFull> itemGetDtoFullList = new ArrayList<>();
        for (Item item : items) {
            Optional<Booking> lastBooking = getLastBooking(bookings, date, item);
            Optional<Booking> nextBooking = getNextBooking(bookings, date, item);
            List<Comment> itemComments = getItemComments(item, comments);
            itemGetDtoFullList.add(toItemGetDtoFull(item, lastBooking, nextBooking, itemComments));
        }
        return itemGetDtoFullList;
    }

    private static List<Comment> getItemComments(Item item, List<Comment> comments) {
        return comments.stream()
                .filter(comment -> comment.getItem().getId() == item.getId())
                .collect(Collectors.toList());
    }

    private static Optional<Booking> getNextBooking(List<Booking> bookings, LocalDateTime date, Item item) {
        return bookings.stream()
                .filter(b -> b.getItem().getId() == item.getId() && b.getState() != BookingState.REJECTED)
                .sorted(Comparator.comparing(Booking::getStart))
                .filter(booking -> booking.getStart().isAfter(date))
                .findFirst();
    }

    private static Optional<Booking> getLastBooking(List<Booking> bookings, LocalDateTime date, Item item) {
        return bookings.stream()
                .filter(b -> b.getItem().getId() == item.getId() && b.getState() != BookingState.REJECTED)
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .filter(booking -> booking.getStart().isBefore(date))
                .findFirst();
    }

    public static ItemGetDtoFull toItemGetDtoFull(Item item,
                                                  List<Booking> bookings,
                                                  LocalDateTime date,
                                                  List<Comment> comments) {
        Optional<Booking> lastBooking = getLastBooking(bookings, date, item);
        Optional<Booking> nextBooking = getNextBooking(bookings, date, item);
        return toItemGetDtoFull(item, lastBooking, nextBooking, comments);
    }

    public static ItemGetDtoFull toItemGetDtoFull(Item item,
                                                  Optional<Booking> lastBooking,
                                                  Optional<Booking> nextBooking,
                                                  List<Comment> comments) {
        ItemGetDtoFull itemGetDtoFull = new ItemGetDtoFull();
        itemGetDtoFull.setId(item.getId());
        itemGetDtoFull.setName(item.getName());
        itemGetDtoFull.setDescription(item.getDescription());
        itemGetDtoFull.setAvailable(item.getAvailable());
        itemGetDtoFull.setOwnerId(item.getUser().getId());
        lastBooking.ifPresent(booking -> itemGetDtoFull.setLastBooking(BookingMapper.toBookingShortDto(booking)));
        nextBooking.ifPresent(booking -> itemGetDtoFull.setNextBooking(BookingMapper.toBookingShortDto(booking)));
        itemGetDtoFull.setComments(CommentMapper.toCommentGetDto(comments));
        return itemGetDtoFull;
    }
}

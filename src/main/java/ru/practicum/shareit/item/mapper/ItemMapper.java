package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
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

    public static List<ItemGetDtoOwner> toItemGetDtoOwner(List<Item> items, List<Booking> bookings, LocalDateTime date) {
        List<ItemGetDtoOwner> itemGetDtoOwnerList = new ArrayList<>();
        for (Item item : items) {
            Optional<Booking> lastBooking = getLastBooking(bookings, date, item);
            Optional<Booking> nextBooking = getNextBooking(bookings, date, item);
            itemGetDtoOwnerList.add(toItemGetDtoOwner(item, lastBooking, nextBooking));
        }
        return itemGetDtoOwnerList;
    }

    private static Optional<Booking> getNextBooking(List<Booking> bookings, LocalDateTime date, Item item) {
        return bookings.stream()
                .filter(b -> b.getItem().getId() == item.getId())
                .sorted(Comparator.comparing(Booking::getStart))
                .filter(booking -> booking.getStart().isAfter(date))
                .findFirst();
    }

    private static Optional<Booking> getLastBooking(List<Booking> bookings, LocalDateTime date, Item item) {
        return bookings.stream()
                .filter(b -> b.getItem().getId() == item.getId())
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .filter(booking -> booking.getStart().isBefore(date))
                .findFirst();
    }

    public static ItemGetDtoOwner toItemGetDtoOwner(Item item, List<Booking> bookings, LocalDateTime date) {
        Optional<Booking> lastBooking = getLastBooking(bookings, date, item);
        Optional<Booking> nextBooking = getNextBooking(bookings, date, item);
        return toItemGetDtoOwner(item, lastBooking, nextBooking);
    }

    public static ItemGetDtoOwner toItemGetDtoOwner(Item item, Optional<Booking> lastBooking, Optional<Booking> nextBooking) {
        ItemGetDtoOwner itemGetDtoOwner = new ItemGetDtoOwner();
        itemGetDtoOwner.setId(item.getId());
        itemGetDtoOwner.setName(item.getName());
        itemGetDtoOwner.setDescription(item.getDescription());
        itemGetDtoOwner.setAvailable(item.getAvailable());
        itemGetDtoOwner.setOwnerId(item.getUser().getId());
        lastBooking.ifPresent(booking -> itemGetDtoOwner.setLastBooking(BookingMapper.toBookingShortDto(booking)));
        nextBooking.ifPresent(booking -> itemGetDtoOwner.setNextBooking(BookingMapper.toBookingShortDto(booking)));
        return itemGetDtoOwner;
    }
}

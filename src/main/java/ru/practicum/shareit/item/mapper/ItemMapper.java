package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemGetDto;
import ru.practicum.shareit.item.dto.ItemPatchDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ItemMapper {
    private final ModelMapper modelMapper;

    public Item toEntity(ItemDto itemDto, User user) {
        Item item = modelMapper.map(itemDto, Item.class);
        item.setUser(user);
        return item;
    }

    public Item toEntity(ItemPatchDto itemPatchDto, Item oldItem) {
        Item item = new Item(oldItem);
        Optional.ofNullable(itemPatchDto.getAvailable()).ifPresent(item::setAvailable);
        Optional.ofNullable(itemPatchDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemPatchDto.getName()).ifPresent(item::setName);
        return item;
    }

    public ItemGetDto toItemGetDto(Item item) {
        ItemGetDto itemGetDto = new ItemGetDto();
        itemGetDto.setId(item.getId());
        itemGetDto.setName(item.getName());
        itemGetDto.setDescription(item.getDescription());
        itemGetDto.setAvailable(item.getAvailable());
        itemGetDto.setOwnerId(item.getUser().getId());
        return itemGetDto;
    }

    public List<ItemGetDto> toItemGetDto(List<Item> items) {
        List<ItemGetDto> itemGetDtoList = new ArrayList<>();
        for (Item item : items) {
            itemGetDtoList.add(toItemGetDto(item));
        }
        return itemGetDtoList;
    }
}

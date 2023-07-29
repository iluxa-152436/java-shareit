package ru.practicum.shareit.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {
}

package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequesterId(long requesterId);
    List<ItemRequest> findByRequesterIdNot(long requesterId, Pageable pageableWithSorting);
    List<ItemRequest> findByRequesterIdNot(long requesterId);
}

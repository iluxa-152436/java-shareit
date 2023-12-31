package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {
    List<Item> findByUserIdOrderById(long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where (upper(it.name) like upper (concat('%', ?1, '%')) or upper(it.description) like upper(concat('%', ?1, '%'))) " +
            "and it.available = true")
    List<Item> findAvailableByNameOrDescription(String text);

    List<Item> findByItemRequestRequesterId(long requesterId);

    List<Item> findByItemRequestRequesterIdNot(long requesterId);
}

package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {
    @Query("select it " +
            "from Item as it " +
            "join it.user as u " +
            "where u.id = ?1")
    List<Item> findByOwnerId(long ownerId);

    @Query("select it " +
            "from Item as it " +
            "where (upper(it.name) like upper (concat('%', ?1, '%')) or upper(it.description) like upper(concat('%', ?1, '%'))) " +
            "and it.available = true")
    List<Item> findAvailableByNameOrDescription(String text);
}

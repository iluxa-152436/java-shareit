package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentStorage extends JpaRepository<Comment, Long> {
    List<Comment> findByItemIdIn(List<Long> itemIds);

    List<Comment> findByItemId(long itemId);
}

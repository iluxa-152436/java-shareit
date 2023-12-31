package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentGetDto;
import ru.practicum.shareit.item.dto.CommentPostDto;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment toEntity(CommentPostDto commentPostDto, User user, Item item) {
        Comment comment = new Comment();
        comment.setText(commentPostDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setUser(user);
        comment.setItem(item);
        return comment;
    }

    public static CommentGetDto toCommentGetDto(Comment comment) {
        return new CommentGetDto(comment.getId(), comment.getText(), comment.getUser().getName(), comment.getCreated());
    }

    public static List<CommentGetDto> toCommentGetDto(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentGetDto).collect(Collectors.toList());
    }
}

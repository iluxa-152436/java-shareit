package ru.practicum.shareit.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ApiErrorMessage;
import ru.practicum.shareit.item.exception.ItemAccessException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserDoesNotExistException;
import ru.practicum.shareit.user.exception.ConflictException;


@Slf4j
@RestControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ApiErrorMessage> handleException(Exception exception) {
        log.debug("Получен статус 400 Bad request {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorMessage> handleExceptionDefaultResponse(MethodArgumentNotValidException exception) {
        log.debug("Получен статус 400 Bad request [{}]", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorMessage(exception.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<ApiErrorMessage> handleConflictException(Exception exception) {
        log.debug("Получен статус 409 Conflict [{}]", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {UserDoesNotExistException.class, ItemNotFoundException.class})
    public ResponseEntity<ApiErrorMessage> handleUserDoesNotExistResponse(Exception exception) {
        log.debug("Получен код 404 Not found [{}]", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {ItemAccessException.class})
    public ResponseEntity<ApiErrorMessage> handleItemAccessException(Exception exception) {
        log.debug("Получен код 403 Forbidden [{}]", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiErrorMessage> handleConstraintViolationException(Exception exception) {
        log.debug("Получен код 409 Conflict [{}]", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorMessage("Ошибка валидации"));
    }
}

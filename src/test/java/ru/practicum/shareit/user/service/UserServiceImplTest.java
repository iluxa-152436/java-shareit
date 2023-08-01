package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserPatchDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl service;
    @Mock
    private UserStorage storage;

    @Test
    void addUser() {
        final User userToSave = User.builder()
                .id(1L)
                .email("email@email.ru")
                .name("name")
                .build();
        when(storage.save(any(User.class))).thenReturn(userToSave);

        final User savedUser = service.addNewUser(new User());

        assertEquals(userToSave, savedUser);
        verify(storage, times(1)).save(any(User.class));
        verifyNoMoreInteractions(storage);
    }

    @Test
    void getUserById() {
        final User expected = User.builder().id(1L).email("email@email.ru").name("name").build();
        when(storage.findById(anyLong())).thenReturn(Optional.of(expected));

        final User received = service.getUserById(anyLong());

        assertEquals(expected, received);
        verify(storage, times(1)).findById(anyLong());
        verifyNoMoreInteractions(storage);
    }

    @Test
    void getUserByIdThatDoesNotExist() {
        when(storage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.getUserById(99L));
        verify(storage, times(1)).findById(99L);
    }

    @Test
    void checkUser() {
        when(storage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.checkUser(99L));
        verify(storage, times(1)).findById(99L);
    }

    @Test
    void updateUser() {
        Long updatedUserId = 1L;
        String email = "updated_email@email.ru";
        String name = "updated name";
        User saved = User.builder().id(updatedUserId).email(email).name(name).build();
        UserPatchDto userPatchDto = new UserPatchDto(name, email);
        when(storage.save(saved)).thenReturn(saved);
        when(storage.findById(anyLong())).thenReturn(Optional.of(saved));

        User updated = service.updateUser(updatedUserId, userPatchDto);

        assertEquals(saved, updated);
        assertEquals(saved.getId(), updated.getId());
        verify(storage, times(1)).findById(updatedUserId);
        verify(storage, times(1)).save(saved);
    }

    @Test
    void getAllUsers() {
        final User expected1 = User.builder().id(1L).email("email1@email.ru").name("name1").build();
        final User expected2 = User.builder().id(2L).email("email2@email.ru").name("name2").build();
        List<User> expectedList = List.of(expected1, expected2);
        when(storage.findAll()).thenReturn(expectedList);

        List<User> users = service.getAllUsers();

        assertEquals(2, users.size());
        assertIterableEquals(expectedList, users);
    }

    @Test
    void deleteUser() {
        doNothing().when(storage).deleteById(anyLong());

        service.deleteUserById(1L);

        verify(storage, times(1)).deleteById(anyLong());
    }
}
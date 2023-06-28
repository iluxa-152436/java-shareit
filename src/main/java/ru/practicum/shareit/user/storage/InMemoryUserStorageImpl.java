package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.ConflictException;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> users;
    private final Map<String, Long> emails;
    private long id;

    @Override
    public User save(User user) {
        if (emails.containsKey(user.getEmail())) {
            throw new ConflictException("User with email " + user.getEmail() + " already exists.");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        emails.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        if (emails.containsKey(user.getEmail())) {
            if (!emails.get(user.getEmail()).equals(user.getId())) {
                throw new ConflictException("User with email " + user.getEmail() + " already exists.");
            }
        }
        User oldUser = users.replace(user.getId(), user);
        emails.remove(oldUser.getEmail());
        emails.put(user.getEmail(), user.getId());
        return user;
    }

    @Override
    public User findById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public void deleteById(long id) {
        emails.remove(users.get(id).getEmail());
        users.remove(id);
    }
}

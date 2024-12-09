package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUserById(int id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        int id = nextId();
        user.setId(id);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUserById(int id) {
        users.remove(id);
    }

    private int nextId() {
        return id++;
    }
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> getUsers();

    User getUserById(int id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(int id);
}

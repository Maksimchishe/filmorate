package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userStorage;

    public Set<Optional<User>> getUsers() {
        return userStorage.getUsers();
    }

    public Optional<User> getUserById(long id) {
        return userStorage.getUserById(id);
    }

    public Optional<User> createUser(User user) {
        return userStorage.createUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }

    public void addFriend(long id, long friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.deleteFriend(id, friendId);
    }

    public Set<Optional<User>> getFriends(long id) {
        return userStorage.getFriends(id);
    }

    public Set<Optional<User>> getCommonFriends(long id, long friendId) {
        return userStorage.getCommonFriends(id, friendId);
    }
}

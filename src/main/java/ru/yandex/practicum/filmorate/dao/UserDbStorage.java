package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDbStorage {

    List<User> getUsers();

    Optional<User> getUserById(long id);

    User createUser(User user);

    User updateUser(User user);

    void deleteUserById(long id);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long id, long friendId);
}

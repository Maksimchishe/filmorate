package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Optional;
import java.util.Set;

public interface UserDbStorage {

    Set<Optional<User>> getUsers();

    Optional<User> getUserById(long id);

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    void deleteUserById(long id);

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    Set<Optional<User>> getFriends(long id);

    Set<Optional<User>> getCommonFriends(long id, long friendId);
}

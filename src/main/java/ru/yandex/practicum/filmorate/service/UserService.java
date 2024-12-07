package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }

    public void addFriend(int id, int friendId) {
        Set<Integer> setUser = userStorage.getUserById(id).getFriendsId();
        setUser.add(friendId);
        User user = userStorage.getUserById(id);
        user.setFriendsId(setUser);
        userStorage.updateUser(user);

        Set<Integer> setFriends = userStorage.getUserById(friendId).getFriendsId();
        setFriends.add(id);
        User friend = userStorage.getUserById(friendId);
        friend.setFriendsId(setFriends);
        userStorage.updateUser(friend);
    }

    public void deleteFriend(int id, int friendId) {
        Set<Integer> setUser = userStorage.getUserById(id).getFriendsId();
        setUser.remove(friendId);
        User user = userStorage.getUserById(id);
        user.setFriendsId(setUser);
        userStorage.updateUser(user);

        Set<Integer> setFriends = userStorage.getUserById(friendId).getFriendsId();
        setFriends.remove(id);
        User friend = userStorage.getUserById(friendId);
        friend.setFriendsId(setFriends);
        userStorage.updateUser(friend);
    }

    public Set<Integer> getFriends(int id) {
        return userStorage.getUserById(id).getFriendsId();
    }

    public Set<Integer> getCommonFriends(int id, int otherId) {
        Set<Integer> setUser = userStorage.getUserById(id).getFriendsId();
        Set<Integer> setFriends = userStorage.getUserById(otherId).getFriendsId();

        setUser.retainAll(setFriends);
        return setUser;
    }

}

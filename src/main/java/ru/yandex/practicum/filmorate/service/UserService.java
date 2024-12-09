package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
        User user = userStorage.getUserById(id);
        Set<Integer> setUser = user.getFriendsId();
        setUser.add(friendId);
        user.setFriendsId(setUser);
        userStorage.updateUser(user);

        User friend = userStorage.getUserById(friendId);
        Set<Integer> setFriends = friend.getFriendsId();
        setFriends.add(id);
        friend.setFriendsId(setFriends);
        userStorage.updateUser(friend);
    }

    public void deleteFriend(int id, int friendId) {
        User user = userStorage.getUserById(id);
        Set<Integer> setUser = user.getFriendsId();
        setUser.remove(friendId);
        user.setFriendsId(setUser);
        userStorage.updateUser(user);

        User friend = userStorage.getUserById(friendId);
        Set<Integer> setFriends = friend.getFriendsId();
        setFriends.remove(id);
        friend.setFriendsId(setFriends);
        userStorage.updateUser(friend);
    }

    public Set<User> getFriendsById(int id) {
        User user = userStorage.getUserById(id);
        Set<Integer> setFriends = new TreeSet<>(user.getFriendsId());

        return setFriends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(int id, int friendId) {
        User user = userStorage.getUserById(id);
        Set<Integer> setUsers = new TreeSet<>(user.getFriendsId());

        User friend = userStorage.getUserById(friendId);
        Set<Integer> setFriends = new TreeSet<>(friend.getFriendsId());
        setUsers.retainAll(setFriends);

        return setUsers.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toSet());
    }

}

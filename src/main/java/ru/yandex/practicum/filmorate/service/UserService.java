package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userStorage;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }

    public User createUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректная форма поля email.");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректная форма поля login.");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будующем");
        }
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userStorage.getUserById(user.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректная форма поля email.");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректная форма поля login.");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будующем");
        }
        return userStorage.updateUser(user);
    }

    public void deleteUserById(long id) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userStorage.deleteUserById(id);
    }

    public void addFriend(long id, long friendId) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        userStorage.deleteFriend(id, friendId);
    }

    public List<User> getFriends(long id) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long friendId) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        return userStorage.getCommonFriends(id, friendId);
    }
}

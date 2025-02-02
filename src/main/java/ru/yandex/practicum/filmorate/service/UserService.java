package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userStorage;

    public Set<UserDto> getUsers() {
        return userStorage.getUsers().stream()
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public UserDto getUserById(long id) {
        return UserMapper.toDto(userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден.")));
    }

    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
            throw new ValidationException("Некорректная форма поля email.");
        }
        if (userDto.getLogin().isBlank() || userDto.getLogin().contains(" ")) {
            throw new ValidationException("Некорректная форма поля login.");
        }
        if (userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }
        if (userDto.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будующем");
        }
        return UserMapper.toDto(userStorage.createUser(UserMapper.toModel(userDto)));
    }

    public UserDto updateUser(UserDto userDto) {
        userStorage.getUserById(userDto.getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        if (userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
            throw new ValidationException("Некорректная форма поля email.");
        }
        if (userDto.getLogin().isBlank() || userDto.getLogin().contains(" ")) {
            throw new ValidationException("Некорректная форма поля login.");
        }
        if (userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }
        if (userDto.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будующем");
        }
        return UserMapper.toDto(userStorage.updateUser(UserMapper.toModel(userDto)));
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

    public Set<UserDto> getFriends(long id) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        return userStorage.getFriends(id).stream()
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<UserDto> getCommonFriends(long id, long friendId) {
        userStorage.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userStorage.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        return userStorage.getCommonFriends(id, friendId).stream()
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

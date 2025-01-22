package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.sql.Date;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Set<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(Optional::get)
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        final User user = userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        return UserMapper.toDto(user);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        if (userDto.getEmail().isBlank() || !userDto.getEmail().contains("@")) {
            throw new ValidationException("Некорректная форма поля email.");
        }
        if (userDto.getLogin().isBlank() || userDto.getLogin().contains(" ")) {
            throw new ValidationException("Некорректная форма поля login.");
        }
        if (userDto.getName().isBlank()) {
            userDto.setName(userDto.getLogin());
        }
        if (userDto.getBirthday().after(Date.from(Instant.now()))) {
            throw new ValidationException("Дата рождения не может быть в будующем");
        }
        final User createUser = userService.createUser(UserMapper.toModel(userDto))
                .orElseThrow(() -> new NotFoundException("Ошибка добавления пользователя."));
        return UserMapper.toDto(createUser);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        final User user = userService.getUserById(userDto.getId())
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
        if (userDto.getBirthday().after(Date.from(Instant.now()))) {
            throw new ValidationException("Дата рождения не может быть в будующем");
        }
        final User updateUser = userService.updateUser(UserMapper.toModel(userDto))
                .orElseThrow(() -> new NotFoundException("Ошибка обновления пользователя."));
        return UserMapper.toDto(updateUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userService.deleteUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userService.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userService.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<UserDto> getFriends(@PathVariable long id) {
        userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        return userService.getFriends(id).stream()
                .map(Optional::get)
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public Set<UserDto> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.getUserById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        userService.getUserById(friendId)
                .orElseThrow(() -> new NotFoundException("Друг не найден."));
        if (id == friendId) {
            throw new NotFoundException("ID пользователя и друга равны.");
        }
        return userService.getCommonFriends(id, friendId).stream()
                .map(u -> UserMapper.toDto(u.get()))
                .sorted(Comparator.comparing(UserDto::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.service.UserService;

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
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable long id) {
        return UserMapper.toDto(userService.getUserById(id));
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto userDto) {
        return UserMapper.toDto(userService.createUser(UserMapper.toModel(userDto)));
    }

    @PutMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return UserMapper.toDto(userService.updateUser(UserMapper.toModel(userDto)));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<UserDto> getFriends(@PathVariable long id) {
        return userService.getFriends(id).stream()
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public Set<UserDto> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        return userService.getCommonFriends(id, friendId).stream()
                .map(UserMapper::toDto)
                .sorted(Comparator.comparing(UserDto::getId).reversed())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

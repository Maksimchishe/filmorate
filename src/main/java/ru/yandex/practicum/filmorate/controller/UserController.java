package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getUsers() {

        if (userService.getUsers().isEmpty()) {
            throw NotFoundException.idUserNotFoundException();
        }

        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == id)) {
            throw NotFoundException.idUserNotFoundException();
        }

        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw ValidationException.emailValidationException();
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw ValidationException.loginValidationException();
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw ValidationException.birthdayValidationException();
        }

        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == user.getId())) {
            throw NotFoundException.idUserNotFoundException();
        }


        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw ValidationException.emailValidationException();
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw ValidationException.loginValidationException();
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw ValidationException.birthdayValidationException();
        }

        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == id)) {
            throw NotFoundException.idUserNotFoundException();
        }

        userService.deleteUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == id)) {
            throw NotFoundException.idUserNotFoundException();
        }

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == friendId)) {
            throw NotFoundException.idFriendNotFoundException();
        }

        if (id == friendId) {
            throw ValidationException.idValidationException();
        }

        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == id)) {
            throw NotFoundException.idUserNotFoundException();
        }

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == friendId)) {
            throw NotFoundException.idFriendNotFoundException();
        }

        if (id == friendId) {
            throw ValidationException.idValidationException();
        }

        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Set<Integer> getFriends(@PathVariable int id) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == id)) {
            throw NotFoundException.idUserNotFoundException();
        }

        return userService.getFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public Set<Integer> getCommonFriends(@PathVariable int id, @PathVariable int friendId) {

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == id)) {
            throw NotFoundException.idUserNotFoundException();
        }

        if (userService.getUsers().stream().noneMatch(f -> f.getId() == friendId)) {
            throw NotFoundException.idFriendNotFoundException();
        }

        if (id == friendId) {
            throw ValidationException.idValidationException();
        }

        if (userService.getCommonFriends(id, friendId).isEmpty()) {
            throw NotFoundException.idUserNotFoundException();
        }

        return userService.getCommonFriends(id, friendId);
    }

}

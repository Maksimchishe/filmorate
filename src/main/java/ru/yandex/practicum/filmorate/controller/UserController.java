package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger logUserController = LoggerFactory.getLogger(UserController.class);

    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    private int nextId() {
        return id++;
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        if (users.isEmpty()) {
            logUserController.warn("Список пользователь пуст.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(users.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        if (users.containsKey(id)) {
            return ResponseEntity.ok().body(users.get(id));
        }
        logUserController.warn("Пользователь с id {} не найден.", id);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            logUserController.error("Некорректный формат электронной почты.");
            throw ValidationException.emailValidationException();
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            logUserController.error("Поле Login не заполнено");
            throw ValidationException.loginValidationException();
        }

        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            logUserController.error("Дата рождения позже настоящего времени");
            throw ValidationException.birthdayValidationException();
        }

        int id = nextId();
        user.setId(id);
        users.put(id, user);
        logUserController.info("Пользователь {} успешно добавлен.", user.getName());
        return users.get(id);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {

        if (users.containsKey(user.getId())) {

            if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
                logUserController.error("Некорректный формат электронной почты.");
                throw ValidationException.emailValidationException();
            }

            if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
                logUserController.error("Поле Login не заполнено");
                throw ValidationException.loginValidationException();
            }

            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            if (user.getBirthday().isAfter(LocalDate.now())) {
                logUserController.error("Дата рождения позже настоящего времени");
                throw ValidationException.birthdayValidationException();
            }

            users.put(user.getId(), user);
            logUserController.info("Пользователь {} успешно обновлен.", user.getName());
            return ResponseEntity.ok().body(users.get(user.getId()));
        }
        logUserController.error("Пользователь с id {} не найден.", user.getId());
        return ResponseEntity.notFound().build();
    }

}

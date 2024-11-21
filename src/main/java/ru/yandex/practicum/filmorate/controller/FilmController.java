package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Logger logUserController = LoggerFactory.getLogger(UserController.class);

    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private int nextId() {
        return id++;
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        if (films.isEmpty()) {
            logUserController.warn("Коллекция фильмов пуста.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {

        if (film.getName().isBlank()) {
            logUserController.error("Наименование фильма не заполнено.");
            throw ValidationException.nameValidationException();
        }

        if (film.getDescription().length() > 200) {
            logUserController.error("Максимальная длина описания первышает 200 символов.");
            throw ValidationException.descriptionValidationException();
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            logUserController.error("Дата релиза раньше 28 декабря 1895 года.");
            throw ValidationException.releaseDateValidationException();
        }

        if (film.getDuration().isNegative()) {
            logUserController.error("Продолжительность фильма отрицательное число.");
            throw ValidationException.durationValidationException();
        }

        int id = nextId();
        film.setId(id);
        films.put(film.getId(), film);
        logUserController.info("Фильм с id {} успешно добавлен.", id);
        return films.get(id);
    }

    @PutMapping
    public Film updateFilmById(@RequestBody Film film) {

        if (films.containsKey(film.getId())) {

            if (film.getName().isBlank()) {
                logUserController.error("Наименование фильма не заполнено.");
                throw ValidationException.nameValidationException();
            }

            if (film.getDescription().length() > 200) {
                logUserController.error("Максимальная длина описания первышает 200 символов.");
                throw ValidationException.descriptionValidationException();
            }

            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                logUserController.error("Дата релиза раньше 28 декабря 1895 года.");
                throw ValidationException.releaseDateValidationException();
            }

            if (film.getDuration().isNegative()) {
                logUserController.error("Продолжительность фильма отрицательное число.");
                throw ValidationException.durationValidationException();
            }

            films.put(film.getId(), film);
            logUserController.info("Фильм с id {} успешно обновлен.", film.getId());
            return films.get(film.getId());
        }
        logUserController.error("Фильм с id {} не найден.", film.getId());
        return film;
    }

}

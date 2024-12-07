package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping
    public Collection<Film> getFilms() {

        if (userService.getUsers().isEmpty()) {
            throw NotFoundException.filmNotFoundException();
        }

        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {

        if (film.getName().isBlank()) {
            throw ValidationException.nameValidationException();
        }

        if (film.getDescription().length() > 200) {
            throw ValidationException.descriptionValidationException();
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw ValidationException.releaseDateValidationException();
        }

        if (film.getDuration() < 0) {
            throw ValidationException.durationValidationException();
        }

        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {

        if (!filmService.getFilms().contains(film.getId())) {
            throw NotFoundException.filmNotFoundException();
        }

        if (film.getName().isBlank()) {
            throw ValidationException.nameValidationException();
        }

        if (film.getDescription().length() > 200) {
            throw ValidationException.descriptionValidationException();
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw ValidationException.releaseDateValidationException();
        }

        if (film.getDuration() < 0) {
            throw ValidationException.durationValidationException();
        }

        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {

        if (!filmService.getFilms().contains(id)) {
            throw NotFoundException.filmNotFoundException();
        }

        if (!userService.getUsers().contains(userId)) {
            throw NotFoundException.idUserNotFoundException();
        }

        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {

        if (!filmService.getFilms().contains(id)) {
            throw NotFoundException.filmNotFoundException();
        }

        if (!userService.getUsers().contains(userId)) {
            throw NotFoundException.idUserNotFoundException();
        }

        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(required = false) Integer count) {

        if (userService.getUsers().isEmpty()) {
            throw NotFoundException.filmNotFoundException();
        }

        return filmService.getPopularFilm(count);
    }

}

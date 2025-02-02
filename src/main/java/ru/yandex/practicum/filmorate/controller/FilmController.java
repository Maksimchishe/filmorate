package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public Set<FilmDto> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public FilmDto getFilmById(@PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping("/films")
    public FilmDto createFilm(@RequestBody FilmDto filmDto) {
        return filmService.createFilm(filmDto);
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@RequestBody FilmDto filmDto) {
        return filmService.updateFilm(filmDto);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilmById(@PathVariable long id) {
        filmService.deleteFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Set<FilmDto> getPopularFilm(@RequestParam(required = false) long count) {
        return filmService.getPopularFilm(count);
    }

    @GetMapping("/genres")
    public Set<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable long id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public Set<Mpa> getMpas() {
        return filmService.getMpas();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        return filmService.getMpaById(id);
    }

    @GetMapping("/films/director/{id}")
    public Set<FilmDto> getFilmDirectorSortById(@PathVariable long id, @RequestParam(required = false) String sortBy) {
        return filmService.getFilmDirectorSortById(id, sortBy);
    }
}
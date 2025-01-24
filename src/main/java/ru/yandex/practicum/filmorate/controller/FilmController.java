package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public Set<FilmDto> getFilms() {
        return filmService.getFilms().stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/films/{id}")
    public FilmDto getFilmById(@PathVariable long id) {
        return FilmMapper.toDto(filmService.getFilmById(id));
    }

    @PostMapping("/films")
    public FilmDto createFilm(@RequestBody Film film) {
        return FilmMapper.toDto(filmService.createFilm(film));
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@RequestBody Film film) {
        return FilmMapper.toDto(filmService.updateFilm(film));
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
        return filmService.getPopularFilm(count).stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
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

}
package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreDbStorage;

    public Set<Optional<Film>> getFilms() {
        return filmStorage.getFilms();
    }

    public Optional<Film> getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Optional<Film> createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilmById(long id) {
        filmStorage.deleteFilmById(id);
    }

    public void addLike(long id, long userId) {
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmStorage.deleteLike(id, userId);
    }

    public Set<Optional<Film>> getPopularFilm(long count) {
        return filmStorage.getPopularFilm(count);
    }

    public LinkedHashSet<Genre> getGenres() {
        return genreDbStorage.getGenres();
    }

    public Optional<Genre> getGenreById(long id) {
        return genreDbStorage.getGenreById(id);
    }
}


package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public interface FilmDbStorage {

    List<Film> getFilms();

    Optional<Film> getFilmById(long id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilmById(long id);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Set<Film> getPopularFilm(long count);
}

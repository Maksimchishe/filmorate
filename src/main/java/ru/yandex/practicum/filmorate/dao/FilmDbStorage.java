package ru.yandex.practicum.filmorate.dao;

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

    LinkedHashSet<Film> getPopularFilm(long count);
}

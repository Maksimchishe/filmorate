package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.*;

public interface FilmDbStorage {

    Set<Optional<Film>> getFilms();

    Optional<Film> getFilmById(long id);

    Optional<Film> createFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void deleteFilmById(long id);

    void addLike(long id, long userId);

    void deleteLike(long id, long userId);

    Set<Optional<Film>> getPopularFilm(long count);
}

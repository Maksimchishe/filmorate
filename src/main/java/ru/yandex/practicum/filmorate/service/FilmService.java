package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
    }

    public Film createFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Поле description не должно превышать 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 1895-12-28.");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Время продолжительности фильма не может быть < 0.");
        }
        mpaDbStorage.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Рейтинг не найден."));
        if (validatorGenres(film.getGenres())) {
            throw new ValidationException("Жанр не найден.");
        }
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Поле description не должно превышать 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 1895-12-28.");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Время продолжительности фильма не может быть < 0.");
        }
        mpaDbStorage.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Рейтинг не найден."));
        if (validatorGenres(film.getGenres())) {
            throw new ValidationException("Жанр не найден.");
        }
        return filmStorage.updateFilm(film);
    }

    public void deleteFilmById(long id) {
        filmStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        filmStorage.deleteFilmById(id);
    }

    public void addLike(long id, long userId) {
        filmStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        userStorage.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден."));
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        userStorage.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден."));
        filmStorage.deleteLike(id, userId);
    }

    public Set<Film> getPopularFilm(long count) {
        return filmStorage.getPopularFilm(count);
    }

    public Set<Genre> getGenres() {
        return genreDbStorage.getGenres();
    }

    public Genre getGenreById(long id) {
        return genreDbStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден."));
    }

    public Set<Mpa> getMpas() {
        return mpaDbStorage.getMpas();
    }

    public Mpa getMpaById(long id) {
        return mpaDbStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден."));
    }

    private boolean validatorGenres(Set<Genre> genres) {
        if (genres == null) return false;
        List<Long> genresFilm = genres.stream()
                .map(Genre::getId)
                .toList();
        List<Long> genresBase = new ArrayList<>(genreDbStorage.getGenres().stream()
                .map(Genre::getId)
                .toList());
        genresBase.retainAll(genresFilm);
        return !(genresFilm.size() == genresBase.size());
    }
}


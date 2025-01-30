package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    public Set<FilmDto> getFilms() {
        return filmStorage.getFilms().stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public FilmDto getFilmById(long id) {
        return FilmMapper.toDto(filmStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден.")));
    }

    public FilmDto createFilm(FilmDto filmDto) {
        if (filmDto.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        if (filmDto.getDescription().length() > 200) {
            throw new ValidationException("Поле description не должно превышать 200 символов.");
        }
        if (filmDto.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 1895-12-28.");
        }
        if (filmDto.getDuration() < 0) {
            throw new ValidationException("Время продолжительности фильма не может быть < 0.");
        }

        mpaDbStorage.getMpaById(filmDto.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден."));

        if (filmDto.getGenres() != null) {
            filmDto.getGenres().forEach(g -> genreDbStorage.getGenreById(g.getId())
                    .orElseThrow(() -> new NotFoundException(":Жанр не найден."))
            );
        }

        return FilmMapper.toDto(filmStorage.createFilm(FilmMapper.toModel(filmDto)));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        filmStorage.getFilmById(filmDto.getId())
                .orElseThrow(() -> new NotFoundException("Фильм не найден."));
        if (filmDto.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        if (filmDto.getDescription().length() > 200) {
            throw new ValidationException("Поле description не должно превышать 200 символов.");
        }
        if (filmDto.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 1895-12-28.");
        }
        if (filmDto.getDuration() < 0) {
            throw new ValidationException("Время продолжительности фильма не может быть < 0.");
        }

        mpaDbStorage.getMpaById(filmDto.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден."));

        if (filmDto.getGenres() != null) {
            filmDto.getGenres().forEach(g -> genreDbStorage.getGenreById(g.getId())
                    .orElseThrow(() -> new NotFoundException(":Жанр не найден."))
            );
        }

        return FilmMapper.toDto(filmStorage.updateFilm(FilmMapper.toModel(filmDto)));
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

    public Set<FilmDto> getPopularFilm(long count) {
        return filmStorage.getPopularFilm(count).stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
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

}


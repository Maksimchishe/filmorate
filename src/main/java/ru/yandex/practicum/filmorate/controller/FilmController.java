package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class FilmController {
    private final FilmService filmService;
    private final UserService userService;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @GetMapping("/films")
    public Set<FilmDto> getFilms() {
        return filmService.getFilms().stream()
                .map(Optional::get)
                .map(FilmMapper::toDto)
                .sorted(Comparator.comparing(FilmDto::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/films/{id}")
    public FilmDto getFilmById(@PathVariable long id) {
        final Film film = filmService.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        return FilmMapper.toDto(film);
    }

    @PostMapping("/films")
    public FilmDto createFilm(@RequestBody FilmDto filmDto) {
        if (filmDto.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        if (filmDto.getDescription().length() > 200) {
            throw new ValidationException("Поле description не должно превышать 200 символов.");
        }
        if (filmDto.getReleaseDate().before(Date.valueOf("1895-12-28"))) {
            throw new ValidationException("Дата релиза не может быть раньше 1895-12-28.");
        }
        if (filmDto.getDuration() < 0) {
            throw new ValidationException("Время продолжительности фильма не может быть < 0.");
        }
        mpaDbStorage.getMpaById(filmDto.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Рейтинг не найден."));
        if (validatorGenres(filmDto.getGenres())) {
            throw new ValidationException("Жанр не найден.");
        }
        return FilmMapper.toDto(filmService.createFilm(FilmMapper.toModel(filmDto)).get());
    }

    @PutMapping("/films")
    public FilmDto updateFilm(@RequestBody FilmDto filmDto) {
        filmService.getFilmById(filmDto.getId())
                .orElseThrow(() -> new NotFoundException("Фтльм не найден."));
        if (filmDto.getName().isBlank()) {
            throw new ValidationException("Поле name не может быть пустым.");
        }
        if (filmDto.getDescription().length() > 200) {
            throw new ValidationException("Поле description не должно превышать 200 символов.");
        }
        if (filmDto.getReleaseDate().before(Date.valueOf("1895-12-28"))) {
            throw new ValidationException("Дата релиза не может быть раньше 1895-12-28.");
        }
        if (filmDto.getDuration() < 0) {
            throw new ValidationException("Время продолжительности фильма не может быть < 0.");
        }
        mpaDbStorage.getMpaById(filmDto.getMpa().getId())
                .orElseThrow(() -> new ValidationException("Рейтинг не найден."));
        if (validatorGenres(filmDto.getGenres())) {
            throw new ValidationException("Жанр не найден.");
        }
        return FilmMapper.toDto(filmService.updateFilm(FilmMapper.toModel(filmDto)).get());
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilmById(@PathVariable long id) {
        filmService.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        filmService.deleteFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        userService.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден."));
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        userService.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден."));
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Set<FilmDto> getPopularFilm(@RequestParam(required = false) long count) {
        return filmService.getPopularFilm(count).stream()
                .map(Optional::get)
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/genres")
    public Set<Genre> getGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable long id) {
        genreDbStorage.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр не найден."));
        return filmService.getGenreById(id).get();
    }

    @GetMapping("/mpa")
    public Set<Mpa> getMpas() {
        return mpaDbStorage.getMpas().stream()
                .map(Optional::get)
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable long id) {
        return mpaDbStorage.getMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг не найден."));
    }

    private boolean validatorGenres(LinkedHashSet<Genre> genres) {
        if (genres == null) return false;

        List<Long> listFilm = new ArrayList<>(genres.stream()
                .map(Genre::getId)
                .toList());
        List<Long> listBase = new ArrayList<>(filmService.getGenres().stream()
                .map(Genre::getId)
                .toList());
        listBase.retainAll(listFilm);
        return !(listFilm.size() == listBase.size());
    }
}
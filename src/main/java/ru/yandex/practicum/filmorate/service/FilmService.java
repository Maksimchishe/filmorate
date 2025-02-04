package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final DirectorDbStorage directorDbStorage;

    public Set<FilmDto> getFilms() {
        return filmDbStorage.getFilms().stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public FilmDto getFilmById(long id) {
        return FilmMapper.toDto(filmDbStorage.getFilmById(id)
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
        if (filmDto.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата релиза не может быть в будущем");
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

        return FilmMapper.toDto(filmDbStorage.createFilm(FilmMapper.toModel(filmDto)));
    }

    public FilmDto updateFilm(FilmDto filmDto) {
        filmDbStorage.getFilmById(filmDto.getId())
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

        return FilmMapper.toDto(filmDbStorage.updateFilm(FilmMapper.toModel(filmDto)));
    }

    public void deleteFilmById(long id) {
        filmDbStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        filmDbStorage.deleteFilmById(id);
    }

    public void addLike(long id, long userId) {
        filmDbStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        userDbStorage.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден."));
        filmDbStorage.addLike(id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmDbStorage.getFilmById(id)
                .orElseThrow(() -> new ValidationException("Фтльм не найден."));
        userDbStorage.getUserById(userId)
                .orElseThrow(() -> new ValidationException("Пользователь не найден."));
        filmDbStorage.deleteLike(id, userId);
    }

    public Set<FilmDto> getPopularFilm(long count, long genreId, long year) {




        LinkedHashSet<Film> filmList = filmDbStorage.getPopularFilm(count);
        for (Film film : filmList) {
            film.setDirectors(directorDbStorage.getDirectorsFilmById(film.getId()));
        }
        return filmList.stream()
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

    public LinkedHashSet<FilmDto> getFilmDirectorSortById(long id, String sortBy) {
        directorDbStorage.getDirectorById(id)
                .orElseThrow(() -> new NotFoundException("Режиссер не найден."));

        if (sortBy.equals("year")) {
            return filmDbStorage.getFilms().stream()
                    .filter(f -> f.getDirectors().stream()
                            .anyMatch(d -> d.getId() == id))
                    .sorted(Comparator.comparing(Film::getReleaseDate)/*.reversed()*/)
                    .map(FilmMapper::toDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else if (sortBy.equals("likes")) {
            return filmDbStorage.getPopularFilm(Long.MAX_VALUE).stream()
                    .filter(f -> f.getDirectors().stream()
                            .anyMatch(d -> d.getId() == id))
                    .map(FilmMapper::toDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        } else {
            return null;
        }
    }
}


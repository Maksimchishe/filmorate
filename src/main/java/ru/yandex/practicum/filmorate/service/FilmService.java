package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> setLike = film.getLike();
        setLike.add(userId);
        film.setLike(setLike);
        filmStorage.updateFilm(film);
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        Set<Integer> setLike = film.getLike();
        setLike.remove(userId);
        film.setLike(setLike);
        filmStorage.updateFilm(film);
    }

    public List<Film> getPopularFilm(int count) {
        Set<Film> films = new TreeSet<>(new PopularComparator());
        films.addAll(filmStorage.getFilms());

        return films.stream()
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toList());
    }

    static class PopularComparator implements Comparator<Film> {
        @Override
        public int compare(Film f1, Film f2) {
            return Integer.compare(f2.getLike().size(), f1.getLike().size());
        }
    }
}


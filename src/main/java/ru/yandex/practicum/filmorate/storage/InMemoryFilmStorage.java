package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 1;

    private final Map<Integer, Film> films = new HashMap<>();

    private int nextId() {
        return id++;
    }

    @Override
    public Collection<Film> getFilms() {
        return films.values();
    }

    @Override
    public Film createFilm(Film film) {

        int id = nextId();
        film.setId(id);
        films.put(film.getId(), film);
        return films.get(id);
    }

    @Override
    public Film updateFilm(Film film) {

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

}

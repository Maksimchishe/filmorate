package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.Optional;

public interface GenreDbStorage {

    void createGenresForFilmById(long filmId, LinkedHashSet<Genre> genres);

    void updateGenresForFilmById(long filmId, LinkedHashSet<Genre> genres);

    void deleteGenreForFilmById(long id);

    LinkedHashSet<Genre> getGenres();

    Optional<Genre> getGenreById(long id);

    LinkedHashSet<Genre> getGenresFilmById(long id);
}

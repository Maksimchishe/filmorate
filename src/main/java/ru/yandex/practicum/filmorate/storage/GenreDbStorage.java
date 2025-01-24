package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.LinkedHashSet;
import java.util.Optional;

public interface GenreDbStorage {

    LinkedHashSet<Genre> getGenres();

    Optional<Genre> getGenreById(long id);

    LinkedHashSet<Genre> getGenresUserById(long id);
}

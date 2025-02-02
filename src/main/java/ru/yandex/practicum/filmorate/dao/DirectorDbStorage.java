package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public interface DirectorDbStorage {

    List<Director> getDirectors();

    Optional<Director> getDirectorById(long id);

    Director createDirector(Director director);

    Director updateDirector(Director director);

    void deleteDirectorById(long id);

    void createDirectorsForFilmById(long filmId, LinkedHashSet<Director> directors);

    LinkedHashSet<Director> getDirectorsFilmById(long id);

    void deleteDirectorsFilmById(long filmId);
}

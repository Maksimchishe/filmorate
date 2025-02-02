package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Repository
@RequiredArgsConstructor
public class DatabaseGenreStorage implements GenreDbStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreRowMapper;

    @Override
    public LinkedHashSet<Genre> getGenres() {
        String sqlQuery = "SELECT id, name FROM Genres";
        return jdbc.query(sqlQuery, genreRowMapper).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "SELECT id, name FROM Genres WHERE id = :id";
        Genre genre = null;
        try {
            genre = jdbc.queryForObject(sqlQuery, params, genreRowMapper);
        } catch (DataAccessException e) {
            Optional.empty();
        }
        return Optional.ofNullable(genre);
    }

    @Override
    public void createGenresForFilmById(long filmId, LinkedHashSet<Genre> genres) {
        String sqlGenres = """
                INSERT INTO Genres_save(film_id, genre_id)
                VALUES (:film_id, :genre_id)
                """;
        SqlParameterSource[] mapGenres = genres.stream()
                .map(e -> new MapSqlParameterSource()
                        .addValue("film_id", filmId)
                        .addValue("genre_id", e.getId())
                )
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(sqlGenres, mapGenres);
    }

    @Override
    public void deleteGenreForFilmById(long id) {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("film_id", id);
            String sqlQuery = "DELETE FROM Genres_save WHERE film_id = :film_id";
            jdbc.update(sqlQuery, params);
    }

    @Override
    public LinkedHashSet<Genre> getGenresFilmById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", id);
        String sqlQuery = """
                SELECT id, name FROM Genres WHERE id IN(
                SELECT genre_id FROM Genres_save WHERE film_id = :film_id)
                """;
        return jdbc.query(sqlQuery, params, genreRowMapper).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

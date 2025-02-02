package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDbStorage;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.mappers.DirectorRowMapper;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Repository
@RequiredArgsConstructor
public class DatabaseDirectorStorage implements DirectorDbStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final DirectorRowMapper directorRowMapper;

    @Override
    public List<Director> getDirectors() {
        String sqlQuery = """
                SELECT id, name FROM Directors
                """;
        return jdbc.query(sqlQuery, directorRowMapper);
    }

    @Override
    public Optional<Director> getDirectorById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = """
                SELECT
                id,
                name
                FROM Directors WHERE id = :id
                """;
        Director director = null;
        try {
            director = jdbc.queryForObject(sqlQuery, params, directorRowMapper);
        } catch (DataAccessException e) {
            Optional.empty();
        }
        return Optional.ofNullable(director);
    }

    @Override
    public Director createDirector(Director director) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", director.getName());
        String sqlFilms = """
                INSERT INTO Directors(name)
                VALUES (:name)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sqlFilms, params, keyHolder, new String[]{"id"});

        director.setId(keyHolder.getKeyAs(Integer.class));
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", director.getId());
        params.addValue("name", director.getName());

        String sqlQuery = """
                    UPDATE Directors SET
                    name = :name
                    WHERE id = :id
                """;
        jdbc.update(sqlQuery, params);

        return director;
    }

    @Override
    public void deleteDirectorById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "DELETE FROM Directors WHERE id = :id";
        jdbc.update(sqlQuery, params);
    }

    @Override
    public void createDirectorsForFilmById(long filmId, LinkedHashSet<Director> directors) {
        String sqlmapDirectorss = """
                INSERT INTO Directors_save(film_id, director_id)
                VALUES (:film_id, :director_id)
                """;
        SqlParameterSource[] mapDirectors = directors.stream()
                .map(d -> new MapSqlParameterSource()
                        .addValue("film_id", filmId)
                        .addValue("director_id", d.getId())
                )
                .toArray(SqlParameterSource[]::new);
        jdbc.batchUpdate(sqlmapDirectorss, mapDirectors);
    }

    @Override
    public LinkedHashSet<Director> getDirectorsFilmById(long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        String sqlQuery = """
                SELECT id, name FROM Directors WHERE id IN(
                SELECT director_id FROM Directors_save WHERE film_id = :film_id)
                """;
        return jdbc.query(sqlQuery, params, directorRowMapper).stream()
                .sorted(Comparator.comparing(Director::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public void deleteDirectorsFilmById(long filmId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        String sqlQuery = """
                DELETE FROM Directors_save WHERE film_id = :film_id
                """;
        jdbc.update(sqlQuery, params);
    }
}

package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DirectorDbStorage;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Repository
@RequiredArgsConstructor
public class DatabaseFilmStorage implements FilmDbStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper filmRowMapper;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final DirectorDbStorage directorDbStorage;

    @Override
    public List<Film> getFilms() {
        String sqlQuery = """
                SELECT id, name, description, releaseDate, duration, mpa_id FROM Films
                """;
        List<Film> filmList = jdbc.query(sqlQuery, filmRowMapper);
        for (Film film : filmList) {
            film.setDirectors(directorDbStorage.getDirectorsFilmById(film.getId()));
        }
        return filmList;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = """
                SELECT
                id,
                name,
                description,
                releaseDate,
                duration,
                mpa_id
                FROM Films WHERE id = :id
                """;
        Film film = null;
        try {
            film = jdbc.queryForObject(sqlQuery, params, filmRowMapper);
            film.setDirectors(directorDbStorage.getDirectorsFilmById(film.getId()));
        } catch (DataAccessException e) {
            Optional.empty();
        }
        return Optional.ofNullable(film);
    }

    @Override
    public Film createFilm(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());
        String sqlFilms = """
                INSERT INTO Films(name, description, releaseDate, duration, mpa_id)
                VALUES (:name, :description, :releaseDate, :duration, :mpa_id)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sqlFilms, params, keyHolder, new String[]{"id"});

        film.setId(keyHolder.getKeyAs(Integer.class));
        film.setMpa(mpaDbStorage.getMpaById(film.getMpa().getId()).get());

        if (film.getGenres() != null) {
            genreDbStorage.createGenresForFilmById(film.getId(), film.getGenres());
            film.setGenres(genreDbStorage.getGenresFilmById(film.getId()));
        }

        if (film.getDirectors() != null) {
            directorDbStorage.createDirectorsForFilmById(film.getId(), film.getDirectors());
            film.setDirectors(directorDbStorage.getDirectorsFilmById(film.getId()));
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", film.getId());
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("mpa_id", film.getMpa().getId());

        String sqlQuery = """
                    UPDATE Films SET
                    name = :name,
                    description = :description,
                    releaseDate = :releaseDate,
                    duration = :duration,
                    mpa_id = :mpa_id
                    WHERE id = :id
                """;
        jdbc.update(sqlQuery, params);

        film.setMpa(mpaDbStorage.getMpaById(film.getMpa().getId()).get());

        if (film.getGenres() != null) {
            genreDbStorage.deleteGenreForFilmById(film.getId());
            genreDbStorage.createGenresForFilmById(film.getId(), film.getGenres());
            film.setGenres(genreDbStorage.getGenresFilmById(film.getId()));
        }

        if (film.getDirectors() != null) {
            directorDbStorage.deleteDirectorsFilmById(film.getId());
            directorDbStorage.createDirectorsForFilmById(film.getId(), film.getDirectors());
            film.setDirectors(directorDbStorage.getDirectorsFilmById(film.getId()));
        }

        return film;
    }

    @Override
    public void deleteFilmById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "DELETE FROM Films WHERE id = :id";
        jdbc.update(sqlQuery, params);
    }

    @Override
    public void addLike(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", id);
        params.addValue("user_id", userId);

        String sqlQuery = """
                INSERT INTO Likes(user_id, film_id) VALUES(:user_id, :film_id)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sqlQuery, params, keyHolder, new String[]{"id"});
    }

    @Override
    public void deleteLike(long id, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", id);
        params.addValue("user_id", userId);
        String sqlQuery = "DELETE FROM Likes WHERE film_id = :film_id AND user_id = :user_id";
        jdbc.update(sqlQuery, params);
    }

    @Override
    public LinkedHashSet<Film> getPopularFilm(long count) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limitMax", count);
        String sqlQuery = """
                SELECT F.*, COUNT(L.film_id) AS count
                FROM Films AS F
                JOIN Likes AS L ON L.film_id = F.id
                GROUP BY F.id
                ORDER BY count DESC
                LIMIT :limitMax
                """;
        return jdbc.queryForStream(sqlQuery, params, filmRowMapper)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public List<Long> getCommonFilmSortByUserIdAndFriendId(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", userId);
        params.addValue("friend_id", friendId);
        String sqlQuery = """
                SELECT film_id
                FROM(
                SELECT film_id, COUNT(film_id) count
                                FROM Likes
                                WHERE user_id = :user_id OR user_id = :friend_id
                                GROUP BY film_id
                )
                WHERE count > 1
                """;
        return jdbc.queryForList(sqlQuery, params, Long.class);
    }
}

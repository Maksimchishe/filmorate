package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
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

    @Override
    public List<Film> getFilms() {
        String sqlQuery = """
                SELECT id, name, description, releaseDate, duration, mpa_id FROM Films
                """;
        return jdbc.query(sqlQuery, filmRowMapper);
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
        List<Film> films = jdbc.query(sqlQuery, params, filmRowMapper);
        if (films.size() == 1) {
            return Optional.ofNullable(films.getFirst());
        }
        return Optional.empty();
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
            String sqlGenres = """
                    INSERT INTO Genres_save(user_id, genre_id)
                    VALUES (:user_id, :genre_id)
                    """;
            SqlParameterSource[] mapGenres = film.getGenres().stream()
                    .map(e -> new MapSqlParameterSource()
                            .addValue("user_id", film.getId())
                            .addValue("genre_id", e.getId())
                    )
                    .toArray(SqlParameterSource[]::new);
            jdbc.batchUpdate(sqlGenres, mapGenres);

            film.setGenres(genreDbStorage.getGenresUserById(film.getId()).stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
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

        if (film.getMpa().getId() != getFilmById(film.getId()).get().getMpa().getId()) {
            throw new NotFoundException("Неверный рейтинг");
        }
        film.setMpa(mpaDbStorage.getMpaById(film.getMpa().getId()).get());

        if (film.getGenres() != null) {
            String sqlGenres = """
                    INSERT INTO Genres_save(user_id, genre_id)
                    VALUES (:user_id, :genre_id)
                    """;
            SqlParameterSource[] mapGenres = film.getGenres().stream()
                    .map(e -> new MapSqlParameterSource()
                            .addValue("user_id", film.getId())
                            .addValue("genre_id", e.getId())
                    )
                    .toArray(SqlParameterSource[]::new);
            jdbc.batchUpdate(sqlGenres, mapGenres);

            film.setGenres(genreDbStorage.getGenresUserById(film.getId()).stream()
                    .sorted(Comparator.comparing(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
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
    public Set<Film> getPopularFilm(long count) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limitMax", count);
        String sqlQuery = """
                SELECT F.*, COUNT(L.film_id) as count
                FROM Films AS F
                JOIN Likes AS L ON L.film_id = F.id
                GROUP BY F.id
                ORDER BY count DESC
                LIMIT :limitMax
                """;
        return jdbc.queryForStream(sqlQuery, params, filmRowMapper)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

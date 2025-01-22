package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Genre> genres = jdbc.query(sqlQuery, params, genreRowMapper);
        if (genres.size() == 1) {
            return Optional.ofNullable(genres.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public LinkedHashSet<Genre> getGenresUserById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        String sqlQuery = """
                SELECT id, name FROM Genres WHERE id IN(
                SELECT genre_id FROM Genres_save WHERE user_id = :user_id)
                """;
        return jdbc.query(sqlQuery, params, genreRowMapper).stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

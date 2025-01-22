package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DatabaseMpaStorage implements MpaDbStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Set<Optional<Mpa>> getMpas() {
        String sqlQuery = "SELECT id, name FROM Ratings";
        return jdbc.query(sqlQuery, mpaRowMapper).stream()
                .map(Optional::of)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Mpa> getMpaById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "SELECT id, name FROM Ratings WHERE id = :id";
        List<Mpa> mpas = jdbc.query(sqlQuery, params, mpaRowMapper);
        if (mpas.size() == 1) {
            return Optional.of(mpas.getFirst());
        }
        return Optional.empty();
    }
}

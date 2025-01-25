package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Repository
@RequiredArgsConstructor
public class DatabaseMpaStorage implements MpaDbStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Set<Mpa> getMpas() {
        String sqlQuery = "SELECT id, name FROM Ratings";
        return jdbc.query(sqlQuery, mpaRowMapper).stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Optional<Mpa> getMpaById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "SELECT id, name FROM Ratings WHERE id = :id";
        Mpa mpa = null;
        try {
            mpa = jdbc.queryForObject(sqlQuery, params, mpaRowMapper);
        } catch (DataAccessException e) {
            Optional.empty();
        }
        return Optional.ofNullable(mpa);
    }
}

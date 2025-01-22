package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ALL")
@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong(1));
        film.setName(rs.getString(2));
        film.setDescription(rs.getString(3));
        film.setReleaseDate(rs.getDate(4));
        film.setDuration(rs.getInt(5));
        film.setMpa(mpaDbStorage.getMpaById(rs.getInt(6)).get());
        film.setGenres(genreDbStorage.getGenresUserById(rs.getLong(1)));
        return film;
    }
}

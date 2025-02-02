package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DirectorDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ALL")
@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final DirectorDbStorage directorDbStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong(1));
        film.setName(rs.getString(2));
        film.setDescription(rs.getString(3));
        film.setReleaseDate(rs.getDate(4).toLocalDate());
        film.setDuration(rs.getInt(5));
        film.setMpa(mpaDbStorage.getMpaById(rs.getInt(6)).get());
        film.setGenres(genreDbStorage.getGenresFilmById(rs.getLong(1)));
        film.setDirectors(directorDbStorage.getDirectorsFilmById(rs.getLong(1)));

        return film;
    }
}

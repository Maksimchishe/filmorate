package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ALL")
@Component
@RequiredArgsConstructor
public class DirectorRowMapper implements RowMapper<Director> {

    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(rs.getLong(1));
        director.setName(rs.getString(2));

        return director;
    }
}

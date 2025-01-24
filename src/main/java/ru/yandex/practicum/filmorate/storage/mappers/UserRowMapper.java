package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong(1));
        user.setEmail(rs.getString(2));
        user.setLogin(rs.getString(3));
        user.setName(rs.getString(4));
        user.setBirthday(rs.getDate(5).toLocalDate());
        return user;
    }
}

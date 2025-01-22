package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DatabaseUserStorage implements UserDbStorage {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userRowMapper;

    @Override
    public Set<Optional<User>> getUsers() {
        String sqlQuery = "SELECT id, email, login, name, birthday FROM Users ORDER BY id";
        return jdbc.query(sqlQuery, userRowMapper).stream()
                .map(Optional::of)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<User> getUserById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "SELECT id, email, login, name, birthday FROM Users WHERE id = :id";
        List<User> users = jdbc.query(sqlQuery, params, userRowMapper);
        if (users.size() == 1) {
            return Optional.ofNullable(users.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> createUser(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        String sqlQuery = "INSERT INTO Users(email, login, name, birthday) VALUES (:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(sqlQuery, params, keyHolder, new String[]{"id"});
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        String sqlQuery = """
                UPDATE Users SET email = :email, login = :login, name = :name, birthday = :birthday WHERE id = :id
                """;
        jdbc.update(sqlQuery, params);
        return Optional.of(user);
    }

    @Override
    public void deleteUserById(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        String sqlQuery = "DELETE FROM Users WHERE id = :id";
        jdbc.update(sqlQuery, params);
    }

    @Override
    public void addFriend(long id, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", friendId);
        String sqlQuery = "INSERT INTO Friends(user_id, friend_id) VALUES (:user_id, :friend_id)";
        jdbc.update(sqlQuery, params);
    }

    @Override
    public void deleteFriend(long id, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        params.addValue("friend_id", friendId);
        String sqlQuery = "DELETE FROM Friends WHERE user_id = :user_id AND friend_id = :friend_id";
        jdbc.update(sqlQuery, params);
    }

    @Override
    public Set<Optional<User>> getFriends(long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("user_id", id);
        String sqlQuery = """
                SELECT id, email, login, name, birthday
                FROM Users
                WHERE id IN(
                SELECT friend_id
                FROM Friends
                WHERE user_id = :user_id
                )""";
        return jdbc.query(sqlQuery, params, userRowMapper).stream()
                .map(Optional::of)
                .collect(Collectors.toSet());
    }

    @Override
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Set<Optional<User>> getCommonFriends(long id, long friendId) {
        List<Long> usersCollection = new ArrayList<>(getFriends(id).stream()
                .map(Optional::get)
                .map(User::getId)
                .toList());
        List<Long> friendsCollection = new ArrayList<>(getFriends(friendId).stream()
                .map(Optional::get)
                .map(User::getId)
                .toList());
        usersCollection.retainAll(friendsCollection);
        return usersCollection.stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }
}

package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@SuppressWarnings("ALL")
//@Import({DatabaseUserStorage.class,
//        UserRowMapper.class,
//        DatabaseFilmStorage.class,
//        FilmRowMapper.class,
//        DatabaseGenreStorage.class,
//        GenreRowMapper.class,
//        DatabaseMpaStorage.class,
//        MpaRowMapper.class})
//@Transactional
//@JdbcTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//class FilmorateApplicationTests {
//    private final UserDbStorage userStorage;
//    private final FilmDbStorage filmStorage;
//
//    static User getTestUser1() {
//        User user = new User();
//        user.setId(1);
//        user.setEmail("test1@email.com");
//        user.setLogin("TestLogin1");
//        user.setName("NameTest1");
//        user.setBirthday(LocalDate.of(2000,01,01));
//        return user;
//    }
//
//    static User getTestUser2() {
//        User user = new User();
//        user.setId(2);
//        user.setEmail("test2@email.com");
//        user.setLogin("TestLogin2");
//        user.setName("NameTest2");
//        user.setBirthday(LocalDate.of(2000,02,02));
//        return user;
//    }
//
//    static User getTestUser3() {
//        User user = new User();
//        user.setId(3);
//        user.setEmail("test3@email.com");
//        user.setLogin("TestLogin3");
//        user.setName("NameTest3");
//        user.setBirthday(LocalDate.of(2000,03,03));
//        return user;
//    }
//
//    static Film getTestFilm1() {
//        Film film = new Film();
//        film.setId(1);
//        film.setName("name1");
//        film.setDescription("description1");
//        film.setReleaseDate(LocalDate.of(2000,01,01));
//        film.setDuration(10);
//        Mpa mpa = new Mpa();
//        mpa.setId(1);
//        film.setMpa(mpa);
//        film.setGenres(new LinkedHashSet<>());
//        return film;
//    }
//
//    static Film getTestFilm2() {
//        Film film = new Film();
//        film.setId(2);
//        film.setName("name2");
//        film.setDescription("description2");
//        film.setReleaseDate(LocalDate.of(2000,02,02));
//        film.setDuration(20);
//        Mpa mpa = new Mpa();
//        mpa.setId(2);
//        film.setMpa(mpa);
//        film.setGenres(new LinkedHashSet<>());
//        return film;
//    }
//
//    static Film getTestFilm3() {
//        Film film = new Film();
//        film.setId(3);
//        film.setName("name3");
//        film.setDescription("description3");
//        film.setReleaseDate(LocalDate.of(2000,03,03));
//        film.setDuration(30);
//        Mpa mpa = new Mpa();
//        mpa.setId(3);
//        film.setMpa(mpa);
//        film.setGenres(new LinkedHashSet<>());
//        return film;
//    }
//
//    @Test
//    public void testGetUsers() {
//        User testUser1 = getTestUser1();
//        User testUser2 = getTestUser2();
//        User testUser3 = getTestUser3();
//
//        Set<User> users = new LinkedHashSet<>();
//        users.add(testUser1);
//        users.add(testUser2);
//        users.add(testUser3);
//
//        userStorage.createUser(testUser1);
//        userStorage.createUser(testUser2);
//        userStorage.createUser(testUser3);
//
//        Set<User> ranges = new LinkedHashSet<>(userStorage.getUsers());
//        assertEquals(3, ranges.size());
//        assertEquals(users, ranges);
//    }
//
//    @Test
//    public void testGetById() {
//        User testUser1 = getTestUser1();
//        userStorage.createUser(testUser1);
//        Optional<User> user1 = userStorage.getUserById(testUser1.getId());
//        assertThat(user1)
//                .isPresent()
//                .get()
//                .usingRecursiveComparison()
//                .isEqualTo(testUser1);
//    }
//
//    @Test
//    public void testCreateUser() {
//        User testUser1 = getTestUser1();
//        User userOptional = userStorage.createUser(testUser1);
//        assertThat(userOptional)
//                .usingRecursiveComparison()
//                .isEqualTo(userOptional);
//    }
//
//    @Test
//    public void testUpdateUser() {
//        User testUser1 = getTestUser1();
//        User testUser2 = getTestUser2();
//        userStorage.createUser(testUser1);
//        User updateOptional = userStorage.updateUser(testUser2);
//        assertThat(updateOptional)
//                .usingRecursiveComparison()
//                .isEqualTo(testUser2);
//    }
//
//    @Test
//    public void testDeleteUser() {
//        User testUser1 = getTestUser1();
//        userStorage.createUser(testUser1);
//        assertEquals(1, userStorage.getUsers().size());
//        userStorage.deleteUserById(testUser1.getId());
//        assertEquals(0, userStorage.getUsers().size());
//    }
//
//    @Test
//    public void testAddAndGetAndDeleteFriend() {
//        User testUser1 = getTestUser1();
//        User testUser2 = getTestUser2();
//
//        userStorage.createUser(testUser1);
//        userStorage.createUser(testUser2);
//        assertEquals(2, userStorage.getUsers().size());
//
//        userStorage.addFriend(testUser1.getId(), testUser2.getId());
//        List<User> friends = userStorage.getFriends(testUser1.getId());
//        User friend = friends.stream().findFirst().get();
//        assertThat(friend)
//                .usingRecursiveComparison()
//                .isEqualTo(testUser2);
//
//        userStorage.deleteFriend(testUser1.getId(), testUser2.getId());
//        assertEquals(0, userStorage.getFriends(testUser1.getId()).size());
//    }
//
//    @Test
//    public void testGetCommonFriends() {
//        User testUser1 = getTestUser1();
//        User testUser2 = getTestUser2();
//        User testUser3 = getTestUser3();
//
//        userStorage.createUser(testUser1);
//        userStorage.createUser(testUser2);
//        userStorage.createUser(testUser3);
//        assertEquals(3, userStorage.getUsers().size());
//
//        userStorage.addFriend(testUser1.getId(), testUser2.getId());
//        userStorage.addFriend(testUser1.getId(), testUser3.getId());
//        userStorage.addFriend(testUser2.getId(), testUser3.getId());
//
//        List<User> commonFriends = userStorage.getCommonFriends(testUser1.getId(), testUser2.getId());
//        User friends = commonFriends.stream().findFirst().get();
//        assertThat(friends)
//                .usingRecursiveComparison()
//                .isEqualTo(testUser3);
//    }
//
//    @Test
//    public void testGetFilms() {
//        Film testFilm1 = getTestFilm1();
//        Film testFilm2 = getTestFilm2();
//        Film testFilm3 = getTestFilm3();
//
//        filmStorage.createFilm(testFilm1);
//        filmStorage.createFilm(testFilm2);
//        filmStorage.createFilm(testFilm3);
//        assertEquals(3, filmStorage.getFilms().size());
//
//        LinkedHashSet<Film> films = new LinkedHashSet<>();
//        films.add(testFilm1);
//        films.add(testFilm2);
//        films.add(testFilm3);
//
//        Set<Film> ranges = new LinkedHashSet<>(filmStorage.getFilms());
//        assertEquals(3, films.size());
//        assertEquals(films, ranges);
//    }
//
//    @Test
//    public void testGetFilmById() {
//        Film testFilm1 = getTestFilm1();
//        filmStorage.createFilm(testFilm1);
//        assertEquals(1, filmStorage.getFilms().size());
//
//        Optional<Film> getFilm = filmStorage.getFilmById(testFilm1.getId());
//        assertThat(getFilm)
//                .isPresent()
//                .get()
//                .usingRecursiveComparison()
//                .isEqualTo(testFilm1);
//    }
//
//    @Test
//    public void testCreateFilm() {
//        Film testFilm1 = getTestFilm1();
//        Film createFilm = filmStorage.createFilm(testFilm1);
//        assertEquals(1, filmStorage.getFilms().size());
//        assertThat(createFilm)
//                .usingRecursiveComparison()
//                .isEqualTo(testFilm1);
//    }
//
//    @Test
//    public void testUpdateFilm() {
//        Film testFilm1 = getTestFilm1();
//
//        filmStorage.createFilm(testFilm1);
//        assertEquals(1, filmStorage.getFilms().size());
//
//        Film updateFilm = filmStorage.updateFilm(testFilm1);
//        assertThat(updateFilm)
//                .usingRecursiveComparison()
//                .isEqualTo(testFilm1);
//    }
//
//    @Test
//    public void testDeleteFilmById() {
//        Film testFilm1 = getTestFilm1();
//        filmStorage.createFilm(testFilm1);
//        assertEquals(1, filmStorage.getFilms().size());
//        filmStorage.deleteFilmById(testFilm1.getId());
//        assertEquals(0, filmStorage.getFilms().size());
//    }
//
//    @Test
//    public void testAddLike() {
//        User testUser1 = getTestUser1();
//        Film testFilm1 = getTestFilm1();
//
//        userStorage.createUser(testUser1);
//        assertEquals(1, userStorage.getUsers().size());
//        filmStorage.createFilm(testFilm1);
//        assertEquals(1, filmStorage.getFilms().size());
//
//        filmStorage.addLike(testFilm1.getId(), testFilm1.getId());
//        Set<Film> film = filmStorage.getPopularFilm(1);
//        Film popFilm = film.stream().findFirst().get();
//        assertThat(popFilm)
//                .usingRecursiveComparison()
//                .isEqualTo(testFilm1);
//    }
//}


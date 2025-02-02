package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public class FilmMapper {

    public static FilmDto toDto(Film film) {
        FilmDto filmDto = new FilmDto();
        filmDto.setId(film.getId());
        filmDto.setName(film.getName());
        filmDto.setDescription(film.getDescription());
        filmDto.setReleaseDate(film.getReleaseDate());
        filmDto.setDuration(film.getDuration());
        filmDto.setMpa(film.getMpa());
        filmDto.setGenres(film.getGenres());
        filmDto.setDirectors(film.getDirectors());
        return filmDto;
    }

    public static Film toModel(FilmDto filmDto) {
        Film film = new Film();
        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());
        film.setMpa(filmDto.getMpa());
        film.setGenres(filmDto.getGenres());
        film.setDirectors(filmDto.getDirectors());
        return film;
    }
}

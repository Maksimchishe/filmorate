package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.util.LinkedHashSet;

@Data
public class FilmDto {
    private long id;
    private String name;
    private String description;
    private Date releaseDate;
    private Integer duration;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
}

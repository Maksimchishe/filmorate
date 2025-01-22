package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.sql.Date;
import java.util.LinkedHashSet;

@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private Date releaseDate;
    private Integer duration;
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;
}

package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NonNull
public class User {

    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}

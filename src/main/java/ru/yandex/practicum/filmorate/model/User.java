package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Integer> friendsId;

    public User() {
        this.name = "";
        this.friendsId = new TreeSet<>();
    }
}

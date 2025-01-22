package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import java.sql.Date;

@Data
public class UserDto {
    private long id;
    private String email;
    private String login;
    private String name;
    private Date birthday;
}

package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {
    private static final String MSG_FILM_NOT_FOUND = "Фильм не найден.";
    private static final String MSG_ID_USER = "Пользователь не найден.";
    private static final String MSG_ID_FRIEND = "Друг не найден.";

    public NotFoundException(String msg) {
        super(msg);
    }

    public static NotFoundException filmNotFoundException() {
        return new NotFoundException(MSG_FILM_NOT_FOUND);
    }

    public static NotFoundException idUserNotFoundException() {
        return new NotFoundException(MSG_ID_USER);
    }

    public static NotFoundException idFriendNotFoundException() {
        return new NotFoundException(MSG_ID_FRIEND);
    }
}

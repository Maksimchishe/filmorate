package ru.yandex.practicum.filmorate.exception;

public class ValidationException extends RuntimeException {

    private static final String MSG_NAME = "Название не может быть пустым.";
    private static final String MSG_DESCRIPTION = "Максимальная длина описания — 200 символов.";
    private static final String MSG_DATE = "Дата релиза — не раньше 28 декабря 1895 года.";
    private static final String MSG_DURATION = "Продолжительность фильма должна быть положительным числом.";
    private static final String MSG_EMAIL = "Электронная почта не может быть пустой и должна содержать символ @.";
    private static final String MSG_LOGIN = "Логин не может быть пустым и содержать пробелы.";
    private static final String MSG_BIRTHDAY = "Дата рождения не может быть в будущем.";

    public static ValidationException nameValidationException() {
        return new ValidationException(MSG_NAME);
    }

    public static ValidationException descriptionValidationException() {
        return new ValidationException(MSG_DESCRIPTION);
    }

    public static ValidationException releaseDateValidationException() {
        return new ValidationException(MSG_DATE);
    }

    public static ValidationException durationValidationException() {
        return new ValidationException(MSG_DURATION);
    }

    public static ValidationException emailValidationException() {
        return new ValidationException(MSG_EMAIL);
    }

    public static ValidationException loginValidationException() {
        return new ValidationException(MSG_LOGIN);
    }

    public static ValidationException birthdayValidationException() {
        return new ValidationException(MSG_BIRTHDAY);
    }

    public ValidationException(String msg) {
        super(msg);
    }

}

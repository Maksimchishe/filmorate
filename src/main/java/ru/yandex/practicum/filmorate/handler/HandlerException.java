package ru.yandex.practicum.filmorate.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(value = ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> processValidationException(ValidationException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error: ", e.getMessage());
        return response;
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public Map<String, String> processNotFoundException(NotFoundException e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error: ", e.getMessage());
        return response;
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> processAnyException(Throwable e) {
        HashMap<String, String> response = new HashMap<>();
        response.put("error: ", "Неизвестная ошибка.");
        return response;
    }
}

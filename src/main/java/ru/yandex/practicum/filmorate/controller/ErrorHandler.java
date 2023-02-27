package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Map;

@RestControllerAdvice(assignableTypes = {UserController.class, FilmController.class})
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(final NotFoundException exception) {
        log.warn("Произошло исключение NotFound сообщение ошибки '{}'", exception.getMessage());
        return Map.of("NotFound", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String>  handleIncorrectParameterException(final IncorrectParameterException exception) {
        log.warn("Произошло исключение Incorrect Parameter сообщение ошибки '{}'", exception.getMessage());
        return Map.of("IncorrectParameter",String.format("Ошибка с полем \"%s\".", exception.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNullPointerException(final NullPointerException exception) {
        log.warn("Произошло исключение Null сообщение ошибки '{}'", exception.getMessage());
        return Map.of("Null Pointer", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(final ValidationException exception) {
        log.warn("Произошло исключение Validation сообщение ошибки '{}'", exception.getMessage());
        return Map.of("Error Validation", exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleThrowable(final Throwable exception) {
        log.warn("Произошло исключение сообщение ошибки '{}'", exception.getMessage());
        return Map.of("Server Error", exception.getMessage());
    }
}

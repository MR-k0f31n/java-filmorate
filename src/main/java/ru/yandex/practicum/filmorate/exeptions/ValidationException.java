package ru.yandex.practicum.filmorate.exeptions;

import java.io.IOException;

public class ValidationException extends RuntimeException  {
    public ValidationException (String message) {
        super(message);
    }
}

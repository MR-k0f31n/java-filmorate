package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidatorTests {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    final String exceptionDescription = "description: Поле description имеет максимальное число символов: 200";
    final String exceptionReleaseDate = "releaseDate: Поле releaseDate некорректно";
    final String exceptionDayCinemaReleaseDate = "releaseDate: Поле releaseDate некорректно дата первого кино: 28.12.1895";
    final String exceptionDuration = "duration: должно быть больше 0";
    final String exceptionDescriptionIsBlank = "description: Поле description не может быть пустым";
    final String exceptionNameIsBlank = "name: Поле name не может быть пустым";


    @BeforeAll
    public static void createValidatorFactory() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    public void filmCreate_isValidTrue() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertTrue(violations.isEmpty(), "Ошибка теста: фильм полностью корректный, ошибка - " + ex.getMessage());
    }

    @Test
    public void filmCreateNotValidDurationNegative_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(-100L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionDuration, ex.getMessage(), "Ошибка теста: у фильма продолжительность минусовая");
    }

    @Test
    public void filmCreateNotValidDuration0_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(0L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionDuration, ex.getMessage(), "Ошибка теста: у фильма 0 в продолжительности");
    }

    @Test
    public void filmCreateValidDescription200_isValidTrue() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program____" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "______________________200")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertTrue(violations.isEmpty(), "Ошибка теста: фильм корректный 200 символов ровно, ошибка - "
                + ex.getMessage());
    }

    @Test
    public void filmCreateNotValidDescription201_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program____" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "_______________________201")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionDescription, ex.getMessage(), "Ошибка теста: у фильма описание 201 символ");
    }

    @Test
    public void filmCreateNotValidDescriptionIsBlank_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionDescriptionIsBlank, ex.getMessage(), "Ошибка теста: у фильма пустое имя");
    }

    @Test
    public void filmCreateNotValidNameIsBlank_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("  ")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionNameIsBlank, ex.getMessage(), "Ошибка теста: у фильма название пустое");
    }

    @Test
    public void filmCreateNotReleaseDate_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().plusMonths(1))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionReleaseDate, ex.getMessage(), "Ошибка теста: фильм из будущего");
    }

    @Test
    public void filmCreateReleaseDateBeforeCinemaBirth_isValidFalse() {
        Film film = Film.builder()
                .id(1L)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(10L)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionDayCinemaReleaseDate, ex.getMessage(), "Ошибка теста: фильм раньше дня кино");
    }
}

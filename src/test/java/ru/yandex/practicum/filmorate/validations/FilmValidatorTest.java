package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FilmValidatorTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    final String exeptionDescription = "description: Поле description имеет максимальное число символов: 200";
    final String exeptionReleaseDate = "releaseDate: Поле releaseDate некорректно";
    final String exeptionDuration = "duration: должно быть больше 0";
    final String exeptionDescriptionIsBlank = "description: Поле description не может быть пустым";
    final String exeptionNameIsBlank = "name: Поле name не может быть пустым";


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
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertTrue(violations.isEmpty(), "Ошибка теста: фильм полностью корректный, ошибка - " + ex.getMessage());
    }

    @Test
    public void filmCreateNotValidDurationNegative_isValidFalse() {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(-100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exeptionDuration, ex.getMessage(), "Ошибка теста: у фильма продолжительность минусовая");
    }

    @Test
    public void filmCreateNotValidDuration0_isValidFalse() {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(0)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exeptionDuration, ex.getMessage(), "Ошибка теста: у фильма 0 в продолжительности");
    }

    @Test
    public void filmCreateValidDescription200_isValidTrue() {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program____" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "______________________200")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertTrue(violations.isEmpty(), "Ошибка теста: фильм корректный 200 символов ровно, ошибка - "
                + ex.getMessage());
    }

    @Test
    public void filmCreateNotValidDescription201_isValidFalse() {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program____" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "_____________________________________________" +
                        "_______________________201")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exeptionDescription, ex.getMessage(), "Ошибка теста: у фильма описание 201 символ");
    }

    @Test
    public void filmCreateNotValidDescriptionIsBlank_isValidFalse() {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exeptionDescriptionIsBlank, ex.getMessage(), "Ошибка теста: у фильма пустое имя");
    }

    @Test
    public void filmCreateNotValidNameIsBlank_isValidFalse() {
        Film film = Film.builder()
                .id(1)
                .name("  ")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exeptionNameIsBlank, ex.getMessage(), "Ошибка теста: у фильма название пустое");
    }

    @Test
    public void filmCreateNotReleaseDate_isValidFalse() {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().plusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exeptionReleaseDate, ex.getMessage(), "Ошибка теста: фильм из будущего");
    }
}

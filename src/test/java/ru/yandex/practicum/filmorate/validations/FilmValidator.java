package ru.yandex.practicum.filmorate.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmValidator {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

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
    public void FilmCreate_isValidTrue () {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(10)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void FilmCreateNotValidDurationNegative_isValidFalse () {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(-100)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmCreateNotValidDuration0_isValidFalse () {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().minusMonths(1))
                .duration(0)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmCreateValidDescription200_isValidTrue () {
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
        assertTrue(violations.isEmpty());
    }

    @Test
    public void FilmCreateNotValidDescription201_isValidFalse () {
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
        assertFalse(violations.isEmpty());
    }

    @Test
    public void FilmCreateNotReleaseDate_isValidFalse () {
        Film film = Film.builder()
                .id(1)
                .name("Parry Hotter")
                .description("This is a boy who learned to program")
                .releaseDate(LocalDate.now().plusMonths(1))
                .duration(0)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}

package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserValidatorTest {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void createValidatorFactory() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    public void UserCreate_isValidTrue() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void UserNotValidEmail_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono?mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void UserNotValidBirthDay_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void UserNotValidLogin_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Admin istrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void UserValidBirthDayNow_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now())
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}

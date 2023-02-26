package ru.yandex.practicum.filmorate.validations;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTests {
    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    final String exceptionEmail = "email: Поле email не соответствует формату userEmail@email.com";
    final String exceptionBirthday = "birthday: Поле birthday не корректно";
    final String exceptionLogin = "login: Поле login должно содержать только A-Z и 1-0";

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
    public void userCreate_isValidTrue() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertTrue(violations.isEmpty(), "Ошибка теста: пользователь полностью корректный, ошибка - "
                + ex.getMessage());
    }

    @Test
    public void userNotValidEmail_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono?mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionEmail, ex.getMessage(), "Ошибка теста: у пользователя не валидный эмейл");
    }

    @Test
    public void userNotValidEmailIsBlank_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email(" ")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionEmail, ex.getMessage(), "Ошибка теста: у пользователя не пустой эмейл");
    }

    @Test
    public void userNotValidBirthDay_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionBirthday, ex.getMessage(), "Ошибка теста: у пользователя день рождения в будущем");
    }

    @Test
    public void userNotValidLogin_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Admin istrator")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionLogin, ex.getMessage(), "Ошибка теста: у пользователя логин с пробелами");
    }

    @Test
    public void userNotValidLoginIsBlank_isValidFalse() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("  ")
                .name("sudo")
                .birthday(LocalDate.now().minusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertEquals(exceptionLogin, ex.getMessage(), "Ошибка теста: у пользователя логин с пустой");
    }

    @Test
    public void userNotValidLoginIsNull_isValidFalse() {
        try {
            User user = User.builder()
                    .id(1)
                    .email("commono@mail.net")
                    .login(null)
                    .name("sudo")
                    .birthday(LocalDate.now().minusDays(1))
                    .build();
            assertNull(user.getLogin(), "Ошибка теста: у пользователя логин null");
        } catch (NullPointerException ex) {
            assertEquals("login is marked non-null but is null", ex.getMessage(), "Ошибка теста: у пользователя логин null");
        }
    }

    @Test
    public void userValidBirthDayNow_isValidTrue() {
        User user = User.builder()
                .id(1)
                .email("commono@mail.net")
                .login("Administrator")
                .name("sudo")
                .birthday(LocalDate.now())
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Exception ex = new ConstraintViolationException(violations);
        assertTrue(violations.isEmpty(), "Ошибка теста: у пользователя все поля корректны, ошибка - "
                + ex.getMessage());
    }
}

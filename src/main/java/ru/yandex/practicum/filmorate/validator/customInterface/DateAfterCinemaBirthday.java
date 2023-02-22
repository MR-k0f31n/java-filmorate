package ru.yandex.practicum.filmorate.validator.customInterface;

import ru.yandex.practicum.filmorate.validator.DateValidate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidate.class)
@Documented
public @interface DateAfterCinemaBirthday {
    String message() default "{javax.validation.constraints.DateAfterCinemaBirthday.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

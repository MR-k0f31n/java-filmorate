package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.validator.customInterface.DateAfterCinemaBirthday;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidate implements ConstraintValidator<DateAfterCinemaBirthday, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}

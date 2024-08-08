package ru.yandex.practicum.filmorate.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = WithoutSpacesValidator.class)
public @interface WithoutSpaces {
    String message() default "Логин должен быть без пробелов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

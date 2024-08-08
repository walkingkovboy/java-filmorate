package ru.yandex.practicum.filmorate.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

@NoArgsConstructor
public class ReleaseDateValidator implements ConstraintValidator<ReleaseDate, Temporal> {
    private static final LocalDate birthOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(ReleaseDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        return value == null || LocalDate.from(value).isAfter(birthOfCinema);
    }
}

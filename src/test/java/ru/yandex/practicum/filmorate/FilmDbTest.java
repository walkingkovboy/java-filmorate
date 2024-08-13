package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDbTest {

    private final FilmDbStorage filmDbStorage;
    private Film testFilm;

    @BeforeEach
    void addFilm() {
        testFilm = new Film();
        testFilm.setName("Die Hard");
        testFilm.setReleaseDate(LocalDate.of(1988, Month.JULY, 12));
        testFilm.setDuration(120L);
        testFilm.setMpa(new Rating(4L, "R"));
        filmDbStorage.add(testFilm);
    }

    @Test
    void getFilmById() {
        testFilm = filmDbStorage.get(1L);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1L);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("name", "Die Hard");
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1988, Month.JULY, 12));
    }

    @Test
    void removeFilm() {
        testFilm.setId(1L);
        filmDbStorage.remove(testFilm);
        Assertions.assertThatThrownBy(() -> filmDbStorage.get(1L)).isInstanceOf(NotExistException.class);
    }

    @Test
    void updateFilm() {
        Film updateFilm = new Film();
        updateFilm.setId(1L);
        updateFilm.setName("Terminator 2 Judgement Day");
        updateFilm.setReleaseDate(LocalDate.of(1999, Month.JULY, 1));
        updateFilm.setMpa(new Rating(4L, "R"));
        filmDbStorage.update(updateFilm);
        testFilm = filmDbStorage.get(1L);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("id", 1L);
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("name", "Terminator 2 Judgement Day");
        Assertions.assertThat(testFilm).hasFieldOrPropertyWithValue("releaseDate", LocalDate.of(1999, Month.JULY, 1));

    }

}

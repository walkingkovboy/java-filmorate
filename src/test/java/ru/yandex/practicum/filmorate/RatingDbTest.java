package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.impl.RatingDbStorage;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RatingDbTest {

    private final RatingDbStorage ratingDbStorage;
    private Rating testRating;

    @Test
    void getRatingById() {
        testRating = ratingDbStorage.get(1L);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("id", 1L);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    void removeRating() {
        testRating = ratingDbStorage.get(1L);
        ratingDbStorage.remove(testRating);
        Assertions.assertThatThrownBy(() -> ratingDbStorage.get(1L)).isInstanceOf(NotExistException.class);
    }

    @Test
    void updateRating() {
        Rating updateRating = new Rating(1L, "X");
        ratingDbStorage.update(updateRating);
        testRating = ratingDbStorage.get(1L);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("id", 1L);
        Assertions.assertThat(testRating).hasFieldOrPropertyWithValue("name", "X");

    }

}

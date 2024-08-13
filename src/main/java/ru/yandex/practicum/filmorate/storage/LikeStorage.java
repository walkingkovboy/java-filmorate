package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.Collection;

public interface LikeStorage {
    Like getLike(Long filmId, Long userId);

    Like removeLike(Like like);

    Collection<Like> getAllLikesForFilm(Long filmId);

    Like likeFilm(Long filmId, Long userId);

    Like deleteLike(Long id, Long userId);
}

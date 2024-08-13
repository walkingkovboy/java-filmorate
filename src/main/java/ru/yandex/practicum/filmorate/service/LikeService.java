package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Like;

public interface LikeService {
    Like likeFilm(Long id, Long userId);

    Like deleteLike(Long id, Long userId);
}

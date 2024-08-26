package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.service.LikeService;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeStorage likeStorage;

    @Override
    public Like likeFilm(Long filmId, Long userId) {
        return likeStorage.likeFilm(filmId, userId);
    }

    @Override
    public Like deleteLike(Long filmId, Long userId) {
        return likeStorage.deleteLike(filmId,userId);
    }
}


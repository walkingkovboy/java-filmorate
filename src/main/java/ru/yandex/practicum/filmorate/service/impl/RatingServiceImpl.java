package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    @Autowired
    private final RatingStorage ratingStorage;

    @Override
    public Rating get(Long id) {
        return ratingStorage.get(id);
    }

    @Override
    public Rating create(Rating rating) {
        return ratingStorage.add(rating);
    }

    @Override
    public Rating update(Rating rating) {
        return ratingStorage.update(rating);
    }

    @Override
    public Collection<Rating> getAll() {
        return ratingStorage.getAll();
    }
}

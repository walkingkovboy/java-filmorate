package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FriendRequest;

public interface FriendStorage {
    FriendRequest sendFriendRequest(Long userIdFrom, Long userIdTo);

    FriendRequest deleteFriendRequest(Long userIdFrom, Long userIdTo);
}

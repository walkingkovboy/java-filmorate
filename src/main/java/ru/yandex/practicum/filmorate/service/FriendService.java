package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.FriendRequest;

public interface FriendService {
    FriendRequest addFriend(Long userIdFrom, Long friendId);

    FriendRequest deleteFriend(Long userIdFrom, Long userIdTo);
}

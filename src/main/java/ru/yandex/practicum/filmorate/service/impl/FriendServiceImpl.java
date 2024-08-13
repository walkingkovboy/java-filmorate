package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.service.FriendService;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendStorage friendStorage;

    @Override
    public FriendRequest addFriend(Long userIdFrom, Long userIdTo) {
        return friendStorage.sendFriendRequest(userIdFrom, userIdTo);
    }

    @Override
    public FriendRequest deleteFriend(Long userIdFrom, Long userIdTo) {
         friendStorage.deleteFriendRequest(userIdFrom, userIdTo);
        return null;
    }
}

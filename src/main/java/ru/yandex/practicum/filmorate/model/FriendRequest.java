package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FriendRequest {
    private Long userIdFrom;
    private Long userIdTo;
    private boolean friendStatus;

}

package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Genre {
    private Long id;
    @EqualsAndHashCode.Exclude
    String name;

}

package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Genre {
    private Long id;
    @EqualsAndHashCode.Exclude
    private String name;

}

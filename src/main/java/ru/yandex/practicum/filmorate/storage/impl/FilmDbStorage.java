package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier(value = "filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String GET_FILM_QUERY = "select * from films " +
            "left join ratings r on r.rating_id = films.rating_id " +
            "left join film_category fc on fc.film_id = films.film_id " +
            "left join genres g on g.genre_id = fc.genre_id " +
            "where films.film_id = ?";
    private static final String INSERT_FILM_QUERY = "insert into films(name, description, release_date, duration, rating_id) " +
            "values(?,?,?,?,?)";
    private static final String DELETE_FILM_QUERY = "delete from films where film_id = ?";
    private static final String UPDATE_FILM_QUERY = "update films set name = ?, release_date = ?, description = ?, " +
            "duration = ?, rating_id = ? where film_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "select * from films " +
            "inner join ratings r on r.rating_id = films.rating_id";
    private static final String FILM_NOT_EXIST_MESSAGE = "Фильма с id %s не существует";

    private static final String GET_POPULAR_FILMS_QUERY = "SELECT f.film_id AS film_id, f.name AS name, f.description AS description, " +
            "f.release_date AS release_date, f.duration AS duration, f.rating_id AS rating_id, r.rating_name AS rating_name, " +
            "COUNT(l.user_id) AS popularity FROM films f LEFT JOIN likes l ON l.film_id = f.film_id " +
            "JOIN ratings r ON r.rating_id = f.rating_id GROUP BY film_id, name, description, rating_id, rating_name ORDER BY popularity DESC ";

    private static final String INSERT_FILM_GENRES = "insert into film_category(film_id, genre_id) values (?,?)";
    private static final String DELETE_FILM_GENRES = "delete from film_category where film_id = ?";
    private static final String GET_FILM_GENRES = "select * from genres inner join film_category on " +
            "genres.genre_id = film_category.genre_id  where film_id = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film get(Long id) {
        List<Film> films = jdbcTemplate.query(GET_FILM_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs), id);
        if (!films.isEmpty())
            return films.stream().findFirst().get();
        throw new NotExistException(String.format(FILM_NOT_EXIST_MESSAGE, id));
    }

    private Film makeFilmFromComplexTable(ResultSet rs) throws SQLException {
        LocalDate releaseDate = LocalDate.parse(rs.getString("release_date"), DATE_FORMATTER);
        Film film = new Film(rs.getLong("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                releaseDate,
                rs.getLong("duration"));
        film.setMpa(new Rating(rs.getLong("rating_id"), rs.getString("rating_name")));
        film.setGenres(getGenresForFilm(film.getId()));
        return film;
    }

    private List<Genre> getGenresForFilm(Long filmId) {
        return jdbcTemplate.query(GET_FILM_GENRES, (rs, rowNum) -> makeGenre(rs), filmId);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
    }

    @Override
    public Film add(Film film) {
        String releaseDate = film.getReleaseDate().format(DATE_FORMATTER);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_FILM_QUERY, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setString(3, releaseDate);
            statement.setLong(4, film.getDuration());
            statement.setLong(5, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film remove(Film film) {
        if (jdbcTemplate.update(DELETE_FILM_QUERY, film.getId()) > 0)
            return film;
        throw new NotExistException(String.format(FILM_NOT_EXIST_MESSAGE, film.getId()));
    }

    @Override
    public Film update(Film film) {
        if (jdbcTemplate.update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getReleaseDate().format(DATE_FORMATTER),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) > 0) {
            jdbcTemplate.update(DELETE_FILM_GENRES, film.getId());
            for (Genre genre : film.getGenres())
                jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId());
            return film;
        }
        throw new NotExistException(String.format(FILM_NOT_EXIST_MESSAGE, film.getId()));
    }

    @Override
    public Boolean contains(Long id) {
        return null;
    }

    @Override
    public Collection<Film> getAll() {
        return jdbcTemplate.query(GET_ALL_FILMS_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs));
    }

    @Override
    public Collection<Film> getPopular(Long count) {
        List<Film> films = jdbcTemplate.query(GET_POPULAR_FILMS_QUERY, (rs, rowNum) -> makeFilmFromComplexTable(rs));
        return films.stream().limit(count).collect(Collectors.toList());
    }
}


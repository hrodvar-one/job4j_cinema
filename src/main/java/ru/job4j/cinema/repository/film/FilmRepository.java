package ru.job4j.cinema.repository.film;

import ru.job4j.cinema.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> getAll();

    Optional<Film> getById(int id);
}

package ru.job4j.cinema.repository.genre;

import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> getAll();

    Optional<Genre> getById(int id);
}

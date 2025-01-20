package ru.job4j.cinema.repository.session;

import ru.job4j.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

public interface FilmSessionRepository {

    List<FilmSession> getAll();

    Optional<FilmSession> getById(int id);
}

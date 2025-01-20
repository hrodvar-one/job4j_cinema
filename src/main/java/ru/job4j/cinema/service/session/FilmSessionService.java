package ru.job4j.cinema.service.session;

import ru.job4j.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

public interface FilmSessionService {

    List<FilmSession> getAll();

    Optional<FilmSession> getById(int id);
}

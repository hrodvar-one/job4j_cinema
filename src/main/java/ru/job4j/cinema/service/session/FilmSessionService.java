package ru.job4j.cinema.service.session;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

public interface FilmSessionService {

    List<FilmSessionDto> getAll();

    Optional<FilmSessionDto> getById(int id);

    Optional<FilmSession> getFilmSessionById(int id);
}

package ru.job4j.cinema.service.session;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;

import java.util.List;
import java.util.Optional;

public interface FilmSessionService {

//    List<FilmSession> getAll();
    List<FilmSessionDto> getAll();

//    Optional<FilmSession> getById(int id);
    Optional<FilmSessionDto> getById(int id);

    boolean isPlaceTaken(int sessionId, int rowNumber, int placeNumber);
}

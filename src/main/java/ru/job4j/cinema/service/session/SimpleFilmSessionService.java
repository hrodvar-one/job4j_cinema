package ru.job4j.cinema.service.session;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.session.FilmSessionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;

    public SimpleFilmSessionService(FilmSessionRepository filmSessionRepository) {
        this.filmSessionRepository = filmSessionRepository;
    }

    @Override
    public List<FilmSession> getAll() {
        return filmSessionRepository.getAll();
    }

    @Override
    public Optional<FilmSession> getById(int id) {
        return filmSessionRepository.getById(id);
    }
}

package ru.job4j.cinema.service.session;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.hall.HallRepository;
import ru.job4j.cinema.repository.session.FilmSessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public SimpleFilmSessionService(FilmSessionRepository filmSessionRepository,
                                    FilmRepository filmRepository,
                                    HallRepository hallRepository) {
        this.filmSessionRepository = filmSessionRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    @Override
    public List<FilmSessionDto> getAll() {
        List<FilmSession> filmSessions = filmSessionRepository.getAll();

        // Преобразуем List<FilmSession> в List<FilmSessionDto>
        return filmSessions.stream()
                .map(filmSession -> {
                    // Извлекаем filmName и hallName из Optional
                    String filmName = filmRepository.getById(filmSession.getFilmId())
                            .map(Film::getName) // Извлекаем имя из Optional<Film>
                            .orElse("Unknown Film"); // Значение по умолчанию, если Film не найден

                    String hallName = hallRepository.getById(filmSession.getHallsId())
                            .map(Hall::getName) // Извлекаем имя из Optional<Hall>
                            .orElse("Unknown Hall"); // Значение по умолчанию, если Hall не найден

                    // Создаем DTO
                    FilmSessionDto dto = new FilmSessionDto();
                    dto.setId(filmSession.getId());
                    dto.setFilmName(filmName);
                    dto.setHallName(hallName);
                    dto.setStartTime(filmSession.getStartTime());
                    dto.setEndTime(filmSession.getEndTime());
                    dto.setPrice(filmSession.getPrice());

                    return dto; // Возвращаем готовый DTO
                })
                .collect(Collectors.toList()); // Сохраняем все DTO в список
    }

    @Override
    public Optional<FilmSessionDto> getById(int id) {
        return filmSessionRepository.getById(id).map(session -> {
            var film = filmRepository.getById(session.getFilmId()).orElseThrow();
            var hall = hallRepository.getById(session.getHallsId()).orElseThrow();

            return new FilmSessionDto(
                    session.getId(),
                    film.getName(),
                    hall.getName(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getPrice()
            );
        });
    }

    @Override
    public Optional<FilmSession> getFilmSessionById(int id) {
        return filmSessionRepository.getById(id);
    }
}

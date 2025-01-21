package ru.job4j.cinema.service.film;

import ru.job4j.cinema.dto.FilmDto;
import java.util.List;
import java.util.Optional;

public interface FilmService {

    List<FilmDto> getAll();

    Optional<FilmDto> getById(int id);
}

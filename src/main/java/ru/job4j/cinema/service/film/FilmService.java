package ru.job4j.cinema.service.film;

import ru.job4j.cinema.dto.FilmDto;
import java.util.List;

public interface FilmService {

    List<FilmDto> getAll();
}

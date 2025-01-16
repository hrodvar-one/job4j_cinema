package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.service.FilmService;

import java.util.List;

@Controller
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAll() {
        return filmService.getAll();
    }
}

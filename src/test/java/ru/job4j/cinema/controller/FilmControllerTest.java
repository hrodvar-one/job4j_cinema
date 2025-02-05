package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.film.FilmService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @Mock
    private Model model;

    @InjectMocks
    private FilmController filmController;

    @Test
    void whenFilmsExistThenReturnViewWithFilms() {

        List<FilmDto> films = List.of(
                new FilmDto(1,
                        "Film1",
                        "Description1",
                        2022,
                        16,
                        120,
                        "Action",
                        "/path/to/poster1",
                        10),
                new FilmDto(2,
                        "Film2",
                        "Description2",
                        2023,
                        18,
                        130,
                        "Drama",
                        "/path/to/poster2",
                        20)
        );

        when(filmService.getAll()).thenReturn(films);

        String viewName = filmController.getAll(model);

        assertEquals("films/filmlist", viewName);
        verify(model, times(1)).addAttribute("films", films);
        verify(filmService, times(1)).getAll();
    }

    @Test
    void whenNoFilmsThenReturnViewWithEmptyList() {

        when(filmService.getAll()).thenReturn(List.of());

        String viewName = filmController.getAll(model);

        assertEquals("films/filmlist", viewName);
        verify(model, times(1)).addAttribute("films", List.of());
        verify(filmService, times(1)).getAll();
    }
}
package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.hall.HallService;
import ru.job4j.cinema.service.session.FilmSessionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilmSessionControllerTest {

    @Mock
    private FilmSessionService filmSessionService;

    @Mock
    private HallService hallService;

    @Mock
    private Model model;

    @InjectMocks
    private FilmSessionController filmSessionController;

    @Test
    void whenGetAllThenReturnSheduleViewWithFilmSessions() {

        List<FilmSessionDto> filmSessions = List.of(
                new FilmSessionDto(1,
                        "Film1",
                        "Hall1",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(2),
                        500),
                new FilmSessionDto(2,
                        "Film2",
                        "Hall2",
                        LocalDateTime.now(),
                        LocalDateTime.now().plusHours(3),
                        600)
        );
        when(filmSessionService.getAll()).thenReturn(filmSessions);

        String viewName = filmSessionController.getAll(model);

        assertEquals("shedules/shedule", viewName);
        verify(model, times(1)).addAttribute("filmSessions", filmSessions);
        verify(filmSessionService, times(1)).getAll();
    }

    @Test
    void whenGetByIdAndSessionExistsThenReturnTicketBuyView() {

        int sessionId = 1;
        int hallId = 1;
        FilmSessionDto filmSessionDto = new FilmSessionDto(sessionId,
                "Film1",
                "Hall1",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                500);
        FilmSession filmSession = new FilmSession(sessionId,
                10,
                hallId,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(2),
                500);
        Hall hall = new Hall(hallId, "Main Hall",
                5,
                10,
                "Spacious hall with premium seating");

        when(filmSessionService.getById(sessionId)).thenReturn(Optional.of(filmSessionDto));
        when(filmSessionService.getFilmSessionById(sessionId)).thenReturn(Optional.of(filmSession));
        when(hallService.getById(hallId)).thenReturn(Optional.of(hall));

        String viewName = filmSessionController.getById(model, sessionId);

        assertEquals("tickets/buy", viewName);
        verify(model, times(1)).addAttribute("sessionId", sessionId);
        verify(model, times(1)).addAttribute("filmName",
                                                                                filmSessionDto.getFilmName());
        verify(model, times(1)).addAttribute("filmDate",
                                                                                filmSessionDto.getStartTime());
        verify(model, times(1)).addAttribute("price", filmSessionDto.getPrice());
        verify(model, times(1)).addAttribute("rows", List.of(1, 2, 3, 4, 5));
        verify(model, times(1)).addAttribute("places",
                                                                                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    }

    @Test
    void whenGetByIdAndSessionDoesNotExistThenReturnError404() {

        int sessionId = 1;
        when(filmSessionService.getById(sessionId)).thenReturn(Optional.empty());

        String viewName = filmSessionController.getById(model, sessionId);

        assertEquals("errors/404", viewName);
        verify(model, times(1)).addAttribute("message",
                                                        "Данного сеанса не существует, выберите другой!");
    }
}
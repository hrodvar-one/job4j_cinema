package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.session.FilmSessionService;
import ru.job4j.cinema.service.ticket.TicketService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private FilmSessionService filmSessionService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private TicketController ticketController;

    @Test
    void whenBuyTicketSuccessfullyThenRedirectToSuccess() {

        int sessionId = 1, row = 5, place = 8, userId = 123;
        FilmSession filmSession = new FilmSession(sessionId, 10, 2, null, null, 500);
        Ticket ticket = new Ticket(1, sessionId, row, place, userId);

        when(filmSessionService.getFilmSessionById(sessionId)).thenReturn(Optional.of(filmSession));
        when(ticketService.save(sessionId, row, place, userId)).thenReturn(Optional.of(ticket));

        String viewName = ticketController.buyTicket(sessionId, row, place, userId, model, redirectAttributes);

        assertEquals("redirect:/tickets/success", viewName);
        verify(redirectAttributes, times(1)).addFlashAttribute("row", row);
        verify(redirectAttributes, times(1)).addFlashAttribute("place", place);
    }

    @Test
    void whenBuyTicketFailsBecausePlaceIsTakenThenReturnError404() {

        int sessionId = 1, row = 5, place = 8, userId = 123;
        FilmSession filmSession = new FilmSession(sessionId, 10, 2, null, null, 500);

        when(filmSessionService.getFilmSessionById(sessionId)).thenReturn(Optional.of(filmSession));
        when(ticketService.save(sessionId, row, place, userId)).thenReturn(Optional.empty());

        String viewName = ticketController.buyTicket(sessionId, row, place, userId, model, redirectAttributes);

        assertEquals("errors/404", viewName);
        verify(model, times(1)).addAttribute("message",
                "Данное выбранное место уже занято, выберите другое!");
    }

    @Test
    void whenBuyTicketFailsBecauseSessionNotFoundThenReturnError404() {

        int sessionId = 1, row = 5, place = 8, userId = 123;

        when(filmSessionService.getFilmSessionById(sessionId)).thenReturn(Optional.empty());

        String viewName = ticketController.buyTicket(sessionId, row, place, userId, model, redirectAttributes);

        assertEquals("errors/404", viewName);
        verify(model, times(1)).addAttribute("message",
                "Данный сеанс не найден!");
    }

    @Test
    void whenSuccessPageThenReturnSuccessView() {

        String viewName = ticketController.successPage();

        assertEquals("tickets/success", viewName);
    }

    @Test
    void whenSuccessPageThenViewShouldNotBeNullOrUnexpected() {

        String viewName = ticketController.successPage();

        assertNotNull(viewName);
        assertNotEquals("wrongView", viewName);
    }
}
package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.session.FilmSessionService;
import ru.job4j.cinema.service.ticket.TicketService;

import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final FilmSessionService filmSessionService;

    public TicketController(TicketService ticketService, FilmSessionService filmSessionService) {
        this.ticketService = ticketService;
        this.filmSessionService = filmSessionService;
    }

    @PostMapping("/buy")
    public String buyTicket(@RequestParam("sessionId") int sessionId,
                            @RequestParam("row") int row,
                            @RequestParam("place") int place,
                            @RequestParam("userId") int userId,
                            Model model,
                            RedirectAttributes redirectAttributes
                            ) {

        Optional<FilmSession> filmSessionOptional = filmSessionService.getFilmSessionById(sessionId);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Данный сеанс не найден!");
            return "errors/404";
        }

        Optional<Ticket> ticket = ticketService.save(sessionId, row, place, userId);

        if (ticket.isPresent()) {
            redirectAttributes.addFlashAttribute("row", row);
            redirectAttributes.addFlashAttribute("place", place);
            return "redirect:/tickets/success";
        } else {
            model.addAttribute("message", "Данное выбранное место уже занято, выберите другое!");
            return "errors/404";
        }
    }

    @GetMapping("/success")
    public String successPage() {
        return "tickets/success";
    }
}

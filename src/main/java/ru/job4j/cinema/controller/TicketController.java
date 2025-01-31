package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.TicketDto;
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

//    @PostMapping("/buy")
////    public String save(@RequestBody TicketDto ticketDto, Model model) {
//    public String save(@ModelAttribute TicketDto ticketDto, Model model) {
//        Optional<Ticket> ticketOptional = ticketService.save(
//                ticketDto.getSessionId(),
//                ticketDto.getRowNumber(),
//                ticketDto.getPlaceNumber(),
//                ticketDto.getUserId()
//        );
//
//        if (ticketOptional.isEmpty()) {
//            model.addAttribute("message", "Не удалось приобрести билет. Место уже занято");
//            return "errors/404";
//        }
//
//        Ticket ticket = ticketOptional.get();
//        ticketDto.setId(ticket.getId());
//        ticketDto.setSessionId(ticket.getSessionId());
//        ticketDto.setRowNumber(ticket.getRowNumber());
//        ticketDto.setPlaceNumber(ticket.getPlaceNumber());
//        ticketDto.setUserId(ticket.getUserId());
//
//        model.addAttribute("ticket", ticketDto);
//        return "tickets/success";
//    }

    @PostMapping("/buy")
    public String buyTicket(@RequestParam("sessionId") int sessionId,
                            @RequestParam("row") int row,
                            @RequestParam("place") int place,
                            Model model) {

        // Проверка существования сеанса
        Optional<FilmSession> filmSessionOptional = filmSessionService.getFilmSessionById(sessionId);
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Данный сеанс не найден!");
            return "errors/404";
        }

        // Проверка, доступно ли место
        boolean isAvailable = ticketService.isSeatAvailable(sessionId, row, place);
        if (!isAvailable) {
            model.addAttribute("message", "Выбранное место уже занято, выберите другое!");
            return "errors/404";
        }

        // Создание билета
        Ticket ticket = new Ticket(sessionId, row, place);
        ticketService.save(ticket);

        // Перенаправление на страницу успешной покупки
        return "redirect:/tickets/success";
    }

//    @PostMapping("/success")
//    public String success(@RequestParam String row, @RequestParam String place, Model model) {
//        model.addAttribute("row", row);
//        model.addAttribute("place", place);
//        return "tickets/success";
//    }

//    @PostMapping("/success")
//    public String buyTicket(@ModelAttribute TicketDto ticketDto, Model model) {
//        // Здесь можно обработать переданные данные
//        // Например, сохранить билет в базе данных
////        ticketService.save(ticketDto);
//        ticketService.save(ticketDto.getSessionId(), ticketDto.getRowNumber(), ticketDto.getPlaceNumber(), ticketDto.getUserId());
//        System.out.println("Билет куплен: " + ticketDto);
//
//        // Добавьте данные в модель, если нужно передать их на страницу
//        model.addAttribute("ticket", ticketDto);
//
//        // Перенаправьте пользователя на страницу подтверждения
//        return "tickets/success"; // Замените на имя вашей страницы
//    }
}

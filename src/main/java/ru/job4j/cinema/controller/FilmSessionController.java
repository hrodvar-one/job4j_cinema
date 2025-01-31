package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.service.hall.HallService;
import ru.job4j.cinema.service.session.FilmSessionService;
import ru.job4j.cinema.service.ticket.TicketService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;
    private final TicketService ticketService;
    private final HallService hallService;

    public FilmSessionController(FilmSessionService filmSessionService, TicketService ticketService, HallService hallService) {
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
        this.hallService = hallService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", filmSessionService.getAll());
        return "shedules/shedule";
    }

//    @GetMapping("/{id}")
//    public String getById(Model model, @PathVariable int id) {
//        Optional<FilmSessionDto> filmSessionOptional = filmSessionService.getById(id);
//
//        // Проверка наличия FilmSession
//        if (filmSessionOptional.isEmpty()) {
//            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
//            return "errors/404";
//        }
//
//        FilmSessionDto filmSession = filmSessionOptional.get();
//
//        // Получение доступных рядов и мест
//        List<Integer> rows = ticketService.getRowsBySessionId(id); // Предположим, метод возвращает список рядов
//        List<Integer> places = ticketService.getPlacesBySessionId(id); // Предположим, метод возвращает список мест
//
//        // Добавление атрибутов в модель
//        model.addAttribute("filmName", filmSession.getFilmName());
//        model.addAttribute("filmDate", filmSession.getStartTime());
//        model.addAttribute("price", filmSession.getPrice());
//        model.addAttribute("rows", rows);
//        model.addAttribute("places", places);
//
//        return "tickets/buy";
//    }

//    @GetMapping("/{id}")
//    public String getById(Model model, @PathVariable int id) {
//        Optional<FilmSessionDto> filmSessionOptional = filmSessionService.getById(id);
//
//        // Проверка наличия FilmSession
//        if (filmSessionOptional.isEmpty()) {
//            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
//            return "errors/404";
//        }
//
//        FilmSessionDto filmSession = filmSessionOptional.get();
//
//        // Получение всех тикетов для сеанса
//        List<TicketDto> tickets = ticketService.getTicketsBySessionId(id);
//
//        // Извлечение уникальных рядов
//        List<Integer> rows = tickets.stream()
//                .map(TicketDto::getRowNumber) // Получаем номер ряда
//                .distinct()               // Убираем дубликаты
//                .sorted()                 // Сортируем для удобства
//                .toList();
//
//        // Извлечение уникальных мест
//        List<Integer> places = tickets.stream()
//                .map(TicketDto::getPlaceNumber) // Получаем номер места
//                .distinct()                  // Убираем дубликаты
//                .sorted()                    // Сортируем для удобства
//                .toList();
//
//        // Добавление атрибутов в модель
//        model.addAttribute("filmName", filmSession.getFilmName());
//        model.addAttribute("filmDate", filmSession.getStartTime());
//        model.addAttribute("price", filmSession.getPrice());
//        model.addAttribute("rows", rows);
//        model.addAttribute("places", places);
//
//        return "tickets/buy";
//    }

//    @GetMapping("/{id}")
//    public String getById(Model model, @PathVariable int id) {
//        Optional<FilmSessionDto> filmSessionOptional = filmSessionService.getById(id);
//
//        // Проверка наличия FilmSession
//        if (filmSessionOptional.isEmpty()) {
//            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
//            return "errors/404";
//        }
//
//        FilmSessionDto filmSession = filmSessionOptional.get();
//
//        // Получение информации о зале
//        Optional<Hall> hallOptional = hallService.getById(filmSession.getHallId());
//        if (hallOptional.isEmpty()) {
//            model.addAttribute("message", "Зал для данного сеанса не найден!");
//            return "errors/404";
//        }
//
//        Hall hall = hallOptional.get();
//
//        // Генерация рядов и мест
//        List<Integer> rows = IntStream.rangeClosed(1, hall.getRowCount())
//                .boxed()
//                .toList();
//        List<Integer> places = IntStream.rangeClosed(1, hall.getPlaceCount())
//                .boxed()
//                .toList();
//
//        // Добавление атрибутов в модель
//        model.addAttribute("filmName", filmSession.getFilmName());
//        model.addAttribute("filmDate", filmSession.getStartTime());
//        model.addAttribute("price", filmSession.getPrice());
//        model.addAttribute("rows", rows);
//        model.addAttribute("places", places);
//
//        return "tickets/buy";
//    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<FilmSessionDto> filmSessionDtoOptional = filmSessionService.getById(id);

        // Проверка наличия FilmSession
        if (filmSessionDtoOptional.isEmpty()) {
            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
            return "errors/404";
        }

        FilmSessionDto filmSessionDto = filmSessionDtoOptional.get();

        Optional<FilmSession> filmSessionOptional = filmSessionService.getFilmSessionById(id);

        // Проверка наличия FilmSession
        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
            return "errors/404";
        }

        FilmSession filmSession = filmSessionOptional.get();

        int hallId = filmSession.getHallsId();

        // Здесь ошибка
        // Получение информации о зале
        // сюда надо передавать id зала информацию о котором мы хотим получить, а не id сеанса
//        Optional<Hall> hallOptional = hallService.getById(filmSession.getId());
        Optional<Hall> hallOptional = hallService.getById(hallId);

        // Проверочная информация
//        System.out.println("id зала = " + hallOptional + "\n" + "id сеанса = " + filmSession.getId());
//        System.out.println("id зала = " + hallOptional.get().getId() + "\n" + "id сеанса = " + filmSession.getId());
//        System.out.println("id зала = " + hallOptional.get().getId() + " " + "id сеанса = " + filmSession.getId());

        if (hallOptional.isEmpty()) {
            model.addAttribute("message", "Зал для данного сеанса не найден!");
            return "errors/404";
        }

        Hall hall = hallOptional.get();

        // Генерация рядов и мест
        List<Integer> rows = IntStream.rangeClosed(1, hall.getRowCount())
                .boxed()
                .toList();
        List<Integer> places = IntStream.rangeClosed(1, hall.getPlaceCount())
                .boxed()
                .toList();

        // Добавление атрибутов в модель

        model.addAttribute("sessionId", filmSessionDto.getId());

        model.addAttribute("filmName", filmSessionDto.getFilmName());
        model.addAttribute("filmDate", filmSessionDto.getStartTime());
        model.addAttribute("price", filmSessionDto.getPrice());
        model.addAttribute("rows", rows);
        model.addAttribute("places", places);

        return "tickets/buy";
    }
}

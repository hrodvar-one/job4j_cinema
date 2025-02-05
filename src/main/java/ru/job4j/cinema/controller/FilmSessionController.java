package ru.job4j.cinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.service.hall.HallService;
import ru.job4j.cinema.service.session.FilmSessionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;
    private final HallService hallService;

    public FilmSessionController(FilmSessionService filmSessionService, HallService hallService) {
        this.filmSessionService = filmSessionService;
        this.hallService = hallService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("filmSessions", filmSessionService.getAll());
        return "shedules/shedule";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<FilmSessionDto> filmSessionDtoOptional = filmSessionService.getById(id);

        if (filmSessionDtoOptional.isEmpty()) {
            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
            return "errors/404";
        }

        FilmSessionDto filmSessionDto = filmSessionDtoOptional.get();

        Optional<FilmSession> filmSessionOptional = filmSessionService.getFilmSessionById(id);

        if (filmSessionOptional.isEmpty()) {
            model.addAttribute("message", "Данного сеанса не существует, выберите другой!");
            return "errors/404";
        }

        FilmSession filmSession = filmSessionOptional.get();

        int hallId = filmSession.getHallsId();

        Optional<Hall> hallOptional = hallService.getById(hallId);

        if (hallOptional.isEmpty()) {
            model.addAttribute("message", "Зал для данного сеанса не найден!");
            return "errors/404";
        }

        Hall hall = hallOptional.get();

        List<Integer> rows = IntStream.rangeClosed(1, hall.getRowCount())
                .boxed()
                .toList();
        List<Integer> places = IntStream.rangeClosed(1, hall.getPlaceCount())
                .boxed()
                .toList();

        model.addAttribute("sessionId", filmSessionDto.getId());

        model.addAttribute("filmName", filmSessionDto.getFilmName());
        model.addAttribute("filmDate", filmSessionDto.getStartTime());
        model.addAttribute("price", filmSessionDto.getPrice());
        model.addAttribute("rows", rows);
        model.addAttribute("places", places);

        return "tickets/buy";
    }
}

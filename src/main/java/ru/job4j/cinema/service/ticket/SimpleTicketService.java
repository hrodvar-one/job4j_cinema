package ru.job4j.cinema.service.ticket;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;
import ru.job4j.cinema.service.session.FilmSessionService;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;
    private final FilmSessionService filmSessionService;

    public SimpleTicketService(TicketRepository ticketRepository, FilmSessionService filmSessionService) {
        this.ticketRepository = ticketRepository;
        this.filmSessionService = filmSessionService;
    }

    @Override
    public Optional<Ticket> save(int sessionId, int rowNumber, int placeNumber, int userId) {
        if (filmSessionService.isPlaceTaken(sessionId, rowNumber, placeNumber)) {
            return Optional.empty();
        }

        Ticket ticket = new Ticket(0, sessionId, rowNumber, placeNumber, userId);

        return ticketRepository.save(ticket);
    }
}

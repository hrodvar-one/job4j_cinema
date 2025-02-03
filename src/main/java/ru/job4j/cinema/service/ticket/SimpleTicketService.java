package ru.job4j.cinema.service.ticket;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;
import ru.job4j.cinema.service.session.FilmSessionService;

import java.util.List;
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
//        if (filmSessionService.isPlaceTaken(sessionId, rowNumber, placeNumber)) {
        if (isSeatAvailable(sessionId, rowNumber, placeNumber, userId)) {
            return Optional.empty();
        }

//        Ticket ticket = new Ticket(0, sessionId, rowNumber, placeNumber, userId);
//        Ticket ticket = new Ticket(sessionId, rowNumber, placeNumber, userId);

        return ticketRepository.save(sessionId, rowNumber, placeNumber, userId);
    }

    @Override
    public Optional<TicketDto> getById(int id) {
        return ticketRepository.getById(id).map(ticket -> {
            TicketDto ticketDto = new TicketDto();
            ticketDto.setId(ticket.getId());
            ticketDto.setSessionId(ticket.getSessionId());
            ticketDto.setRowNumber(ticket.getRowNumber());
            ticketDto.setPlaceNumber(ticket.getPlaceNumber());
            ticketDto.setUserId(ticket.getUserId());
            return ticketDto;
        });
    }

    @Override
    public List<TicketDto> getTicketsBySessionId(int sessionId) {
        return ticketRepository.getTicketsBySessionId(sessionId)
                .stream()
                .map(ticket -> new TicketDto(
                        ticket.getId(),
                        ticket.getSessionId(),
                        ticket.getRowNumber(),
                        ticket.getPlaceNumber(),
                        ticket.getUserId()
                ))
                .toList();
    }

    @Override
    public boolean isSeatAvailable(int sessionId, int row, int place, int userId) {
        return ticketRepository.isSeatAvailable(sessionId, row, place, userId);
    }

    @Override
    public List<Integer> getRowsBySessionId(int sessionId) {
        return ticketRepository.getRowsBySessionId(sessionId);
    }

    @Override
    public List<Integer> getPlacesBySessionId(int sessionId) {
        return ticketRepository.getPlacesBySessionId(sessionId);
    }
}

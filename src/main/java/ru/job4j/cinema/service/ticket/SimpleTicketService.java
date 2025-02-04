package ru.job4j.cinema.service.ticket;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.ticket.TicketRepository;

import java.util.Optional;

@Service
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Optional<Ticket> save(int sessionId, int rowNumber, int placeNumber, int userId) {
        if (isSeatAvailable(sessionId, rowNumber, placeNumber, userId)) {
            return Optional.empty();
        }

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
    public boolean isSeatAvailable(int sessionId, int row, int place, int userId) {
        return ticketRepository.isSeatAvailable(sessionId, row, place, userId);
    }
}

package ru.job4j.cinema.service.ticket;

import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    Optional<Ticket> save(int sessionId, int rowNumber, int placeNumber, int userId);

    Optional<TicketDto> getById(int id);

    List<TicketDto> getTicketsBySessionId(int sessionId);

    List<Integer> getRowsBySessionId(int sessionId);

    List<Integer> getPlacesBySessionId(int sessionId);
}

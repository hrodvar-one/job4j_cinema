package ru.job4j.cinema.repository.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> save(Ticket ticket);

    Optional<Ticket> getById(int id);

//    boolean isPlaceTaken(int sessionId, int rowNumber, int placeNumber);

    boolean isSeatAvailable(int sessionId, int row, int place);

    List<Ticket> getTicketsBySessionId(int sessionId);

    List<Integer> getRowsBySessionId(int sessionId);

    List<Integer> getPlacesBySessionId(int sessionId);
}

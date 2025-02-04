package ru.job4j.cinema.repository.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> save(int sessionId, int row, int place, int userId);

    Optional<Ticket> getById(int id);

    boolean isSeatAvailable(int sessionId, int row, int place, int userId);
}

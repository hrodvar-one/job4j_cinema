package ru.job4j.cinema.service.ticket;

import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

public interface TicketService {

    Optional<Ticket> save(int sessionId, int rowNumber, int placeNumber, int userId);
}

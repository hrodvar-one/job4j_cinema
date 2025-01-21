package ru.job4j.cinema.repository.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;

import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final Logger LOG = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      
                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                      VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                      """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("filmSessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Sql2oException e) {
            LOG.error("Данное место занято, купите другой билет!");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> getById(int id) {
        return Optional.empty();
    }

    @Override
    public boolean isPlaceTaken(int sessionId, int rowNumber, int placeNumber) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      SELECT COUNT(*) > 0
                      FROM tickets
                      WHERE session_id = :sessionId AND row_number = :rowNumber AND place_number = :placeNumber
                      """;
            Query query = connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .addParameter("rowNumber", rowNumber)
                    .addParameter("placeNumber", placeNumber);
            return query.executeScalar(Boolean.class);
        }
    }
}

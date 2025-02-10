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
    public Optional<Ticket> save(int sessionId, int row, int place, int userId) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      
                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                      VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                      """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("sessionId", sessionId)
                    .addParameter("rowNumber", row)
                    .addParameter("placeNumber", place)
                    .addParameter("userId", userId);
            int generatedId = query.executeUpdate().getKey(Integer.class);

            Ticket ticket = new Ticket(generatedId, sessionId, row, place, userId);

            ticket.setId(generatedId);
            return Optional.of(ticket);
        } catch (Sql2oException e) {
            LOG.error("Данное место занято, купите другой билет!");
        }
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> getById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * FROM tickets WHERE id = :id");
            query.addParameter("id", id);
            Ticket ticket = query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetchFirst(Ticket.class);
            return Optional.ofNullable(ticket);
        }
    }
}

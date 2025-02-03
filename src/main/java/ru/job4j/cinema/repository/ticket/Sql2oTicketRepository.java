package ru.job4j.cinema.repository.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import ru.job4j.cinema.model.Ticket;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oTicketRepository implements TicketRepository {

    private static final Logger LOG = LoggerFactory.getLogger(Sql2oTicketRepository.class.getName());

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

//    @Override
//    public Optional<Ticket> save(Ticket ticket) {
//        try (Connection connection = sql2o.open()) {
//            String sql = """
//
//                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
//                      VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
//                      """;
//            Query query = connection.createQuery(sql, true)
//                    .addParameter("filmSessionId", ticket.getSessionId())
//                    .addParameter("rowNumber", ticket.getRowNumber())
//                    .addParameter("placeNumber", ticket.getPlaceNumber())
//                    .addParameter("userId", ticket.getUserId());
//            int generatedId = query.executeUpdate().getKey(Integer.class);
//            ticket.setId(generatedId);
//            return Optional.of(ticket);
//        } catch (Sql2oException e) {
//            LOG.error("Данное место занято, купите другой билет!");
//        }
//        return Optional.empty();
//    }

    @Override
    public Optional<Ticket> save(int sessionId, int row, int place, int userId) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      
                    INSERT INTO tickets (session_id, row_number, place_number, user_id)
                      VALUES (:sessionId, :rowNumber, :placeNumber, :userId)
                      """;
            Query query = connection.createQuery(sql, true)
                    .addParameter("filmSessionId", sessionId)
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

    @Override
    public boolean isSeatAvailable(int sessionId, int row, int place, int userId) {
        try (Connection connection = sql2o.open()) {
            String sql = """
                      SELECT COUNT(*) > 0
                      FROM tickets
                      WHERE session_id = :sessionId AND row_number = :rowNumber AND place_number = :placeNumber AND user_id = :userId
                      """;
            Query query = connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .addParameter("rowNumber", row)
                    .addParameter("placeNumber", place)
                    .addParameter("userId", userId);
            return query.executeScalar(Boolean.class);
        }
    }

//    @Override
//    public boolean isSeatAvailable(int sessionId, int row, int place, int userId) {
//        return false;
//    }

//    @Override
//    public boolean isPlaceTaken(int sessionId, int rowNumber, int placeNumber) {
//        try (Connection connection = sql2o.open()) {
//            String sql = """
//                      SELECT COUNT(*) > 0
//                      FROM tickets
//                      WHERE session_id = :sessionId AND row_number = :rowNumber AND place_number = :placeNumber
//                      """;
//            Query query = connection.createQuery(sql)
//                    .addParameter("sessionId", sessionId)
//                    .addParameter("rowNumber", rowNumber)
//                    .addParameter("placeNumber", placeNumber);
//            return query.executeScalar(Boolean.class);
//        }
//    }

    @Override
    public List<Ticket> getTicketsBySessionId(int sessionId) {
        try (Connection connection = sql2o.open()) {
            String sql = "SELECT id, session_id AS sessionId, row_number AS rowNumber, place_number AS placeNumber, user_id AS userId "
                    + "FROM tickets WHERE session_id = :sessionId";
            return connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .executeAndFetch(Ticket.class);
        }
    }

    /**
     * Проверяет, занято ли место в указанном сеансе.
     */
    public boolean isSeatAvailable(int sessionId, int row, int place) {
//        String sql = "SELECT COUNT(*) FROM tickets WHERE session_id = :sessionId AND row = :row AND place = :place";
        String sql = "SELECT COUNT(*) FROM tickets WHERE session_id = :sessionId AND row_number = :row AND place_number = :place";

        try (Connection con = sql2o.open()) {
            Integer count = con.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .addParameter("row", row)
                    .addParameter("place", place)
                    .executeScalar(Integer.class);
            return count != null && count == 0; // Если 0 записей, значит место свободно
        }
    }

    @Override
    public List<Integer> getRowsBySessionId(int sessionId) {
        try (Connection connection = sql2o.open()) {
            String sql = "SELECT DISTINCT row_number FROM tickets WHERE session_id = :sessionId";
            return connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .executeAndFetch(Integer.class);
        }
    }

    @Override
    public List<Integer> getPlacesBySessionId(int sessionId) {
        try (Connection connection = sql2o.open()) {
            String sql = "SELECT DISTINCT place_number FROM tickets WHERE session_id = :sessionId";
            return connection.createQuery(sql)
                    .addParameter("sessionId", sessionId)
                    .executeAndFetch(Integer.class);
        }
    }
}

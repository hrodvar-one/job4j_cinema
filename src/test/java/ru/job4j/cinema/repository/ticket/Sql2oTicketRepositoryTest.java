package ru.job4j.cinema.repository.ticket;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oTicketRepositoryTest {

    private static Sql2o sql2o;
    private static Sql2oTicketRepository ticketRepository;

    @BeforeAll
    static void setupDatabase() {
        Properties properties = new Properties();
        try (InputStream input = Sql2oTicketRepositoryTest.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            if (input == null) {
                throw new IOException("Не удалось найти файл connection.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки свойств", e);
        }

        sql2o = new Sql2o(
                properties.getProperty("datasource.url"),
                properties.getProperty("datasource.username"),
                properties.getProperty("datasource.password")
        );
        ticketRepository = new Sql2oTicketRepository(sql2o);
    }

    @BeforeEach
    void clearAndPopulateDatabase() {
        try (Connection con = sql2o.open()) {
            con.createQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            con.createQuery("DELETE FROM tickets").executeUpdate();
            con.createQuery("DELETE FROM film_sessions").executeUpdate();
            con.createQuery("DELETE FROM films").executeUpdate();
            con.createQuery("DELETE FROM halls").executeUpdate();
            con.createQuery("DELETE FROM genres").executeUpdate();
            con.createQuery("DELETE FROM files").executeUpdate();
            con.createQuery("DELETE FROM users").executeUpdate();

            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            con.createQuery("INSERT INTO files (id, name, path) VALUES (1, 'Poster', '/path/to/poster.jpg')")
                    .executeUpdate();

            con.createQuery("INSERT INTO genres (id, name) VALUES (1, 'Action')")
                    .executeUpdate();

            con.createQuery("INSERT INTO halls (id, name, row_count, place_count, description) "
                            + "VALUES (1, 'Main Hall', 10, 20, 'Biggest hall')")
                    .executeUpdate();

            con.createQuery("INSERT INTO films (id, name, description, \"year\", genre_id, minimal_age, duration_in_minutes, file_id) "
                            + "VALUES (1, 'Test Film', 'A great movie', 2024, 1, 16, 120, 1)")
                    .executeUpdate();

            con.createQuery("INSERT INTO film_sessions (id, film_id, halls_id, start_time, end_time, price) "
                            + "VALUES (1, 1, 1, '2025-02-05 10:00:00', '2025-02-05 12:00:00', 500)")
                    .executeUpdate();

            con.createQuery("INSERT INTO users (id, full_name, email, password) "
                            + "VALUES (1, 'Test User', 'test@example.com', 'password')")
                    .executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenSaveTicketThenReturnTicket() {
        Optional<Ticket> ticket = ticketRepository.save(1, 5, 10, 1);
        assertTrue(ticket.isPresent());
        assertEquals(1, ticket.get().getSessionId());
        assertEquals(5, ticket.get().getRowNumber());
        assertEquals(10, ticket.get().getPlaceNumber());
        assertEquals(1, ticket.get().getUserId());
    }

    @Test
    @Order(2)
    void whenSaveDuplicateTicketThenReturnEmpty() {
        ticketRepository.save(1, 5, 10, 1);
        Optional<Ticket> duplicateTicket = ticketRepository.save(1, 5, 10, 1);
        assertTrue(duplicateTicket.isEmpty());
    }

    @Test
    @Order(3)
    void whenGetByIdThenReturnTicket() {
        Optional<Ticket> savedTicket = ticketRepository.save(1, 3, 5, 1);
        assertTrue(savedTicket.isPresent());

        Optional<Ticket> retrievedTicket = ticketRepository.getById(savedTicket.get().getId());
        assertTrue(retrievedTicket.isPresent());
        assertEquals(savedTicket.get(), retrievedTicket.get());
    }

    @Test
    @Order(4)
    void whenGetByIdNotExistThenReturnEmptyOptional() {
        Optional<Ticket> ticket = ticketRepository.getById(999);
        assertTrue(ticket.isEmpty());
    }

    @Test
    @Order(5)
    void whenIsSeatAvailableAndSeatTakenThenReturnTrue() {
        ticketRepository.save(1, 2, 3, 1);
        assertTrue(ticketRepository.isSeatAvailable(1, 2, 3, 1));
    }

    @Test
    @Order(6)
    void whenIsSeatAvailableAndSeatFreeThenReturnFalse() {
        assertFalse(ticketRepository.isSeatAvailable(1, 2, 4, 1));
    }
}
package ru.job4j.cinema.repository.session;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oFilmSessionRepositoryTest {

    private static Sql2o sql2o;
    private static Sql2oFilmSessionRepository sessionRepository;

    @BeforeAll
    static void setupDatabase() {
        Properties properties = new Properties();
        try (InputStream input = Sql2oFilmSessionRepositoryTest.class.getClassLoader()
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
        sessionRepository = new Sql2oFilmSessionRepository(sql2o);
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

            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            con.createQuery("INSERT INTO files (id, name, path) VALUES (1, 'Poster', '/path/to/poster.jpg')")
                    .executeUpdate();
            con.createQuery("INSERT INTO genres (id, name) VALUES (1, 'Action')")
                    .executeUpdate();
            con.createQuery("INSERT INTO halls (id, name, row_count, place_count, description) "
                            + "VALUES (1, 'Main Hall', 10, 20, 'The biggest hall')")
                    .executeUpdate();
            con.createQuery("INSERT INTO films (id, name, description, \"year\", genre_id, minimal_age, duration_in_minutes, file_id) "
                            + "VALUES (1, 'Test Film', 'A great movie', 2024, 1, 16, 120, 1)")
                    .executeUpdate();
            con.createQuery("INSERT INTO film_sessions (id, film_id, halls_id, start_time, end_time, price) "
                            + "VALUES (1, 1, 1, '2025-02-05 10:00:00'::TIMESTAMP, '2025-02-05 12:00:00'::TIMESTAMP, 500)")
                    .executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenGetAllThenReturnFilmSessionList() {
        List<FilmSession> sessions = sessionRepository.getAll();
        assertEquals(1, sessions.size()); // Ожидаем одну запись
        assertEquals(1, sessions.get(0).getFilmId());
        assertEquals(1, sessions.get(0).getHallsId());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 0), sessions.get(0).getStartTime());
    }

    @Test
    @Order(2)
    void whenGetAllAndTableIsEmptyThenReturnEmptyList() {
        try (Connection con = sql2o.open()) {
            con.createQuery("DELETE FROM film_sessions").executeUpdate();
        }
        List<FilmSession> sessions = sessionRepository.getAll();
        assertTrue(sessions.isEmpty());
    }

    @Test
    @Order(3)
    void whenGetByIdThenReturnFilmSession() {
        Optional<FilmSession> session = sessionRepository.getById(1);
        assertTrue(session.isPresent());
        assertEquals(1, session.get().getFilmId());
        assertEquals(1, session.get().getHallsId());
        assertEquals(LocalDateTime.of(2025, 2, 5, 10, 0), session.get().getStartTime());
    }

    @Test
    @Order(4)
    void whenGetByIdNotExistThenReturnEmptyOptional() {
        Optional<FilmSession> session = sessionRepository.getById(999);
        assertTrue(session.isEmpty());
    }
}
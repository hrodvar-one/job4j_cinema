package ru.job4j.cinema.repository.film;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oFilmRepositoryTest {

    private Sql2oFilmRepository filmRepository;
    private Sql2o sql2o;

    @BeforeAll
    void setUpDatabase() throws IOException {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("connection.properties")) {
            if (input == null) {
                throw new IOException("Не найден файл connection.properties");
            }
            properties.load(input);
        }

        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username", "");
        String password = properties.getProperty("datasource.password", "");

        sql2o = new Sql2o(url, username, password);
        filmRepository = new Sql2oFilmRepository(sql2o);
    }

    @BeforeEach
    void clearAndPopulateDatabase() {
        try (Connection con = sql2o.open()) {
            // Отключаем проверку ссылочной целостности
            con.createQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            // Удаляем все связанные данные
            con.createQuery("DELETE FROM tickets").executeUpdate();
            con.createQuery("DELETE FROM film_sessions").executeUpdate();
            con.createQuery("DELETE FROM films").executeUpdate();
            con.createQuery("DELETE FROM files").executeUpdate();
            con.createQuery("DELETE FROM genres").executeUpdate();
            con.createQuery("DELETE FROM halls").executeUpdate();
            con.createQuery("DELETE FROM users").executeUpdate();

            // Включаем обратно проверку ссылочной целостности
            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            // Вставляем тестовые данные
            con.createQuery("INSERT INTO genres (id, name) VALUES (1, 'Action')").executeUpdate();
            con.createQuery("INSERT INTO files (id, name, path) VALUES (1, 'poster.jpg', '/images/poster.jpg')").executeUpdate();
            con.createQuery("INSERT INTO films (id, name, description, \"year\", genre_id, minimal_age, duration_in_minutes, file_id) "
                    + "VALUES (1, 'Test Film', 'Description', 2024, 1, 16, 120, 1)").executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenGetAllThenReturnFilmList() {
        List<Film> films = filmRepository.getAll();
        assertEquals(1, films.size());
        assertEquals("Test Film", films.get(0).getName());
    }

    @Test
    @Order(2)
    void whenGetByIdThenReturnFilm() {
        Optional<Film> film = filmRepository.getById(1);
        assertTrue(film.isPresent());
        assertEquals("Test Film", film.get().getName());
    }

    @Test
    @Order(3)
    void whenGetByIdNotFoundThenReturnEmpty() {
        Optional<Film> film = filmRepository.getById(999);
        assertFalse(film.isPresent());
    }
}
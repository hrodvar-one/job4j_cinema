package ru.job4j.cinema.repository.genre;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oGenreRepositoryTest {

    private static Sql2o sql2o;
    private static Sql2oGenreRepository genreRepository;

    @BeforeAll
    static void setupDatabase() {
        Properties properties = new Properties();
        try (InputStream input = Sql2oGenreRepositoryTest.class.getClassLoader()
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
        genreRepository = new Sql2oGenreRepository(sql2o);
    }

    @BeforeEach
    void clearAndPopulateDatabase() {
        try (Connection con = sql2o.open()) {

            con.createQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            con.createQuery("DELETE FROM films").executeUpdate();
            con.createQuery("DELETE FROM genres").executeUpdate();

            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            con.createQuery("INSERT INTO genres (id, name) VALUES (1, 'Action')").executeUpdate();
            con.createQuery("INSERT INTO genres (id, name) VALUES (2, 'Comedy')").executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenGetAllThenReturnGenreList() {
        List<Genre> genres = genreRepository.getAll();
        assertNotNull(genres);
        assertEquals(2, genres.size());
        assertEquals("Action", genres.get(0).getName());
        assertEquals("Comedy", genres.get(1).getName());
    }

    @Test
    @Order(2)
    void whenGetAllAndTableIsEmptyThenReturnEmptyList() {
        try (Connection con = sql2o.open()) {

            con.createQuery("DELETE FROM films").executeUpdate();
            con.createQuery("DELETE FROM genres").executeUpdate();
        }
        List<Genre> genres = genreRepository.getAll();
        assertTrue(genres.isEmpty());
    }

    @Test
    @Order(3)
    void whenGetByIdThenReturnGenre() {
        Optional<Genre> genre = genreRepository.getById(1);
        assertTrue(genre.isPresent());
        assertEquals("Action", genre.get().getName());
    }

    @Test
    @Order(4)
    void whenGetByIdNotExistThenReturnEmptyOptional() {
        Optional<Genre> genre = genreRepository.getById(999);
        assertTrue(genre.isEmpty());
    }
}
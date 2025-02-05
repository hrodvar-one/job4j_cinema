package ru.job4j.cinema.repository.file;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oFileRepositoryTest {

    private Sql2oFileRepository fileRepository;
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
        fileRepository = new Sql2oFileRepository(sql2o);
    }

    @BeforeEach
    void clearAndPopulateDatabase() {
        try (Connection con = sql2o.open()) {

            con.createQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            con.createQuery("DELETE FROM tickets").executeUpdate();
            con.createQuery("DELETE FROM film_sessions").executeUpdate();
            con.createQuery("DELETE FROM films").executeUpdate();
            con.createQuery("DELETE FROM files").executeUpdate();
            con.createQuery("DELETE FROM genres").executeUpdate();
            con.createQuery("DELETE FROM halls").executeUpdate();
            con.createQuery("DELETE FROM users").executeUpdate();

            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            String sql = "INSERT INTO files (id, name, path) VALUES (1, 'test_file.txt', '/path/to/test_file.txt')";

            con.createQuery(sql).executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenGetAllThenReturnFileList() {
        List<File> files = fileRepository.getAll();
        assertEquals(1, files.size());
        assertEquals("test_file.txt", files.get(0).getName());
    }

    @Test
    @Order(2)
    void whenGetByIdThenReturnFile() {
        Optional<File> file = fileRepository.getById(1);
        assertTrue(file.isPresent());
        assertEquals("test_file.txt", file.get().getName());
    }

    @Test
    @Order(3)
    void whenGetByIdNotFoundThenReturnEmpty() {
        Optional<File> file = fileRepository.getById(999);
        assertFalse(file.isPresent());
    }
}
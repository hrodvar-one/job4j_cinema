package ru.job4j.cinema.repository.hall;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oHallRepositoryTest {

    private static Sql2o sql2o;
    private static Sql2oHallRepository hallRepository;

    @BeforeAll
    static void setupDatabase() {
        Properties properties = new Properties();
        try (InputStream input = Sql2oHallRepositoryTest.class.getClassLoader()
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
        hallRepository = new Sql2oHallRepository(sql2o);
    }

    @BeforeEach
    void clearAndPopulateDatabase() {
        try (Connection con = sql2o.open()) {
            // Отключаем проверку ссылочной целостности
            con.createQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

            // Очищаем связанные таблицы перед удалением залов
            con.createQuery("DELETE FROM film_sessions").executeUpdate();
            con.createQuery("DELETE FROM halls").executeUpdate();

            // Включаем обратно проверку ссылочной целостности
            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            // Вставляем тестовые данные
            con.createQuery("INSERT INTO halls (id, name, row_count, place_count, description) "
                            + "VALUES (1, 'Main Hall', 10, 20, 'The biggest hall')")
                    .executeUpdate();

            con.createQuery("INSERT INTO halls (id, name, row_count, place_count, description) "
                            + "VALUES (2, 'VIP Hall', 5, 10, 'Luxury experience')")
                    .executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenGetByIdThenReturnHall() {
        Optional<Hall> hall = hallRepository.getById(1);
        assertTrue(hall.isPresent());
        assertEquals("Main Hall", hall.get().getName());
        assertEquals(10, hall.get().getRowCount());
        assertEquals(20, hall.get().getPlaceCount());
    }

    @Test
    @Order(2)
    void whenGetByIdNotExistThenReturnEmptyOptional() {
        Optional<Hall> hall = hallRepository.getById(999);
        assertTrue(hall.isEmpty());
    }
}
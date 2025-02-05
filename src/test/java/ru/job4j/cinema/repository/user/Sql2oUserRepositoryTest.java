package ru.job4j.cinema.repository.user;

import org.junit.jupiter.api.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sql2oUserRepositoryTest {

    private static Sql2o sql2o;
    private static Sql2oUserRepository userRepository;

    @BeforeAll
    static void setupDatabase() {
        Properties properties = new Properties();
        try (InputStream input = Sql2oUserRepositoryTest.class.getClassLoader()
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
        userRepository = new Sql2oUserRepository(sql2o);
    }

    @BeforeEach
    void clearAndPopulateDatabase() {
        try (Connection con = sql2o.open()) {
            con.createQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
            con.createQuery("DELETE FROM users").executeUpdate();
            con.createQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

            // Вставляем пользователя без явного указания id (автоинкремент)
            con.createQuery("INSERT INTO users (full_name, email, password) "
                            + "VALUES ('Test User', 'test@example.com', 'password123')")
                    .executeUpdate();
        }
    }

    @Test
    @Order(1)
    void whenSaveUserThenReturnUserWithId() {
        User newUser = new User(0, "John Doe", "john@example.com", "securePass");
        Optional<User> savedUser = userRepository.save(newUser);

        assertTrue(savedUser.isPresent());
        assertNotEquals(0, savedUser.get().getId());
        assertEquals("John Doe", savedUser.get().getFullName());
        assertEquals("john@example.com", savedUser.get().getEmail());
    }

    @Test
    @Order(2)
    void whenSaveDuplicateEmailThenReturnEmpty() {
        User duplicateUser = new User(0, "Test User", "test@example.com", "password123");
        Optional<User> result = userRepository.save(duplicateUser);

        assertTrue(result.isEmpty(), "User with duplicate email should not be saved");
    }

    @Test
    @Order(3)
    void whenFindByEmailAndPasswordThenReturnUser() {
        Optional<User> user = userRepository.findByEmailAndPassword("test@example.com", "password123");

        assertTrue(user.isPresent());
        assertEquals("Test User", user.get().getFullName());
        assertEquals("test@example.com", user.get().getEmail());
    }

    @Test
    @Order(4)
    void whenFindByEmailAndWrongPasswordThenReturnEmpty() {
        Optional<User> user = userRepository.findByEmailAndPassword("test@example.com", "wrongPassword");

        assertTrue(user.isEmpty(), "User with wrong password should not be found");
    }

    @Test
    @Order(5)
    void whenFindByNonExistingEmailThenReturnEmpty() {
        Optional<User> user = userRepository.findByEmailAndPassword("unknown@example.com", "password123");

        assertTrue(user.isEmpty(), "Non-existing user should not be found");
    }
}
package ru.job4j.cinema.service.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SimpleUserService simpleUserService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1, "John Doe", "john.doe@example.com", "password123");
    }

    // ✅ Успешный тест для save()
    @Test
    void whenSaveUserThenReturnUser() {
        // Arrange
        when(userRepository.save(testUser)).thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = simpleUserService.save(testUser);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).save(testUser);
    }

    // ❌ Неуспешный тест для save()
    @Test
    void whenSaveUserFailsThenReturnEmpty() {
        // Arrange
        when(userRepository.save(testUser)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = simpleUserService.save(testUser);

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).save(testUser);
    }

    // ✅ Успешный тест для findByEmailAndPassword()
    @Test
    void whenFindByEmailAndPasswordThenReturnUser() {
        // Arrange
        when(userRepository.findByEmailAndPassword("john.doe@example.com", "password123"))
                .thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = simpleUserService.findByEmailAndPassword("john.doe@example.com", "password123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmailAndPassword("john.doe@example.com", "password123");
    }

    // ❌ Неуспешный тест для findByEmailAndPassword()
    @Test
    void whenFindByEmailAndPasswordFailsThenReturnEmpty() {
        // Arrange
        when(userRepository.findByEmailAndPassword("wrong.email@example.com", "wrongpassword"))
                .thenReturn(Optional.empty());

        // Act
        Optional<User> result = simpleUserService.findByEmailAndPassword("wrong.email@example.com", "wrongpassword");

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository).findByEmailAndPassword("wrong.email@example.com", "wrongpassword");
    }
}
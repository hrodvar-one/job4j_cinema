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

    @Test
    void whenSaveUserThenReturnUser() {

        when(userRepository.save(testUser)).thenReturn(Optional.of(testUser));

        Optional<User> result = simpleUserService.save(testUser);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).save(testUser);
    }

    @Test
    void whenSaveUserFailsThenReturnEmpty() {

        when(userRepository.save(testUser)).thenReturn(Optional.empty());

        Optional<User> result = simpleUserService.save(testUser);

        assertTrue(result.isEmpty());
        verify(userRepository).save(testUser);
    }

    @Test
    void whenFindByEmailAndPasswordThenReturnUser() {

        when(userRepository.findByEmailAndPassword("john.doe@example.com", "password123"))
                .thenReturn(Optional.of(testUser));

        Optional<User> result = simpleUserService.findByEmailAndPassword("john.doe@example.com", "password123");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmailAndPassword("john.doe@example.com", "password123");
    }

    @Test
    void whenFindByEmailAndPasswordFailsThenReturnEmpty() {

        when(userRepository.findByEmailAndPassword("wrong.email@example.com", "wrongpassword"))
                .thenReturn(Optional.empty());

        Optional<User> result = simpleUserService.findByEmailAndPassword("wrong.email@example.com", "wrongpassword");

        assertTrue(result.isEmpty());
        verify(userRepository).findByEmailAndPassword("wrong.email@example.com", "wrongpassword");
    }
}
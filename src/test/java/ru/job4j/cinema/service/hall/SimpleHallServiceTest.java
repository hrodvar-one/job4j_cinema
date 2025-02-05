package ru.job4j.cinema.service.hall;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.hall.HallRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleHallServiceTest {

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private SimpleHallService simpleHallService;

    @Test
    void whenGetByIdThenReturnHall() {
        // Arrange
        Hall expectedHall = new Hall(1,
                "Main Hall",
                5,
                10,
                "Large hall with a big screen");
        when(hallRepository.getById(1)).thenReturn(Optional.of(expectedHall));

        // Act
        Optional<Hall> result = simpleHallService.getById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedHall, result.get());
        verify(hallRepository, times(1)).getById(1);
    }

    @Test
    void whenGetByIdAndHallNotFoundThenReturnEmpty() {
        // Arrange
        when(hallRepository.getById(2)).thenReturn(Optional.empty());

        // Act
        Optional<Hall> result = simpleHallService.getById(2);

        // Assert
        assertFalse(result.isPresent());
        verify(hallRepository, times(1)).getById(2);
    }
}
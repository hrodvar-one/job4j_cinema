package ru.job4j.cinema.service.session;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.hall.HallRepository;
import ru.job4j.cinema.repository.session.FilmSessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleFilmSessionServiceTest {

    @Mock
    private FilmSessionRepository filmSessionRepository;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private HallRepository hallRepository;

    @InjectMocks
    private SimpleFilmSessionService simpleFilmSessionService;

    private final FilmSession testSession = new FilmSession(
            1, 1, 1, LocalDateTime.of(2024, 2, 5, 10, 0),
            LocalDateTime.of(2024, 2, 5, 12, 0), 500);

    private final Film testFilm = new Film(1,
            "Test Film",
            "Description",
            2024,
            1,
            16,
            120,
            1);

    private final Hall testHall = new Hall(1, "Main Hall", 10, 20, "Large hall");

    @Test
    void whenGetAllThenReturnFilmSessionDtoList() {
        // Arrange
        when(filmSessionRepository.getAll()).thenReturn(List.of(testSession));
        when(filmRepository.getById(1)).thenReturn(Optional.of(testFilm));
        when(hallRepository.getById(1)).thenReturn(Optional.of(testHall));

        // Act
        List<FilmSessionDto> result = simpleFilmSessionService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testSession.getId(), result.get(0).getId());
        assertEquals("Test Film", result.get(0).getFilmName());
        assertEquals("Main Hall", result.get(0).getHallName());
        assertEquals(testSession.getStartTime(), result.get(0).getStartTime());
        assertEquals(testSession.getEndTime(), result.get(0).getEndTime());
        assertEquals(testSession.getPrice(), result.get(0).getPrice());
    }

    @Test
    void whenGetAllAndNoSessionsThenReturnEmptyList() {
        // Arrange
        when(filmSessionRepository.getAll()).thenReturn(List.of());

        // Act
        List<FilmSessionDto> result = simpleFilmSessionService.getAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void whenGetByIdThenReturnFilmSessionDto() {
        // Arrange
        when(filmSessionRepository.getById(1)).thenReturn(Optional.of(testSession));
        when(filmRepository.getById(1)).thenReturn(Optional.of(testFilm));
        when(hallRepository.getById(1)).thenReturn(Optional.of(testHall));

        // Act
        Optional<FilmSessionDto> result = simpleFilmSessionService.getById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Film", result.get().getFilmName());
        assertEquals("Main Hall", result.get().getHallName());
        assertEquals(testSession.getStartTime(), result.get().getStartTime());
        assertEquals(testSession.getEndTime(), result.get().getEndTime());
        assertEquals(testSession.getPrice(), result.get().getPrice());
    }

    @Test
    void whenGetByIdAndNotFoundThenReturnEmpty() {
        // Arrange
        when(filmSessionRepository.getById(2)).thenReturn(Optional.empty());

        // Act
        Optional<FilmSessionDto> result = simpleFilmSessionService.getById(2);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void whenGetFilmSessionByIdThenReturnFilmSession() {
        // Arrange
        when(filmSessionRepository.getById(1)).thenReturn(Optional.of(testSession));

        // Act
        Optional<FilmSession> result = simpleFilmSessionService.getFilmSessionById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(testSession, result.get());
    }

    @Test
    void whenGetFilmSessionByIdAndNotFoundThenReturnEmpty() {
        // Arrange
        when(filmSessionRepository.getById(2)).thenReturn(Optional.empty());

        // Act
        Optional<FilmSession> result = simpleFilmSessionService.getFilmSessionById(2);

        // Assert
        assertFalse(result.isPresent());
    }
}
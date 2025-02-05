package ru.job4j.cinema.service.film;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.file.FileRepository;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.genre.GenreRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleFilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private SimpleFilmService simpleFilmService;

    private Film testFilm;
    private Genre testGenre;
    private File testFile;
    private FilmDto expectedFilmDto;

    @BeforeEach
    void setUp() {
        testFilm = new Film(1, "Inception",
                "A mind-bending thriller",
                2010,
                1,
                16,
                148,
                1);
        testGenre = new Genre(1, "Sci-Fi");
        testFile = new File(1, "inception.jpg", "/images/inception.jpg");
        expectedFilmDto = new FilmDto(
                testFilm.getId(),
                testFilm.getName(),
                testFilm.getDescription(),
                testFilm.getYear(),
                testFilm.getMinimalAge(),
                testFilm.getDurationInMinutes(),
                testGenre.getName(),
                testFile.getPath(),
                testFilm.getFileId()
        );
    }

    @Test
    void whenGetAllThenReturnFilmDtoList() {
        // Arrange
        List<Film> films = List.of(testFilm);
        when(filmRepository.getAll()).thenReturn(films);
        when(genreRepository.getById(1)).thenReturn(Optional.of(testGenre));
        when(fileRepository.getById(1)).thenReturn(Optional.of(testFile));

        // Act
        List<FilmDto> result = simpleFilmService.getAll();

        // Assert
        assertEquals(expectedFilmDto.getId(), result.get(0).getId());
        assertEquals(expectedFilmDto.getName(), result.get(0).getName());
        assertEquals(expectedFilmDto.getDescription(), result.get(0).getDescription());
        assertEquals(expectedFilmDto.getYear(), result.get(0).getYear());
        assertEquals(expectedFilmDto.getMinimalAge(), result.get(0).getMinimalAge());
        assertEquals(expectedFilmDto.getDurationInMinutes(), result.get(0).getDurationInMinutes());
        assertEquals(expectedFilmDto.getGenre(), result.get(0).getGenre());
        assertEquals(expectedFilmDto.getPosterPath(), result.get(0).getPosterPath());
        assertEquals(expectedFilmDto.getFileId(), result.get(0).getFileId());
    }

    @Test
    void whenGetAllAndNoFilmsThenReturnEmptyList() {
        // Arrange
        when(filmRepository.getAll()).thenReturn(List.of());

        // Act
        List<FilmDto> result = simpleFilmService.getAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void whenGetByIdThenReturnFilmDto() {
        // Arrange
        when(filmRepository.getById(1)).thenReturn(Optional.of(testFilm));
        when(genreRepository.getById(1)).thenReturn(Optional.of(testGenre));
        when(fileRepository.getById(1)).thenReturn(Optional.of(testFile));

        // Act
        Optional<FilmDto> result = simpleFilmService.getById(1);

        // Assert
        assertTrue(result.isPresent());

        FilmDto actualFilm = result.get();
        assertEquals(expectedFilmDto.getId(), actualFilm.getId());
        assertEquals(expectedFilmDto.getName(), actualFilm.getName());
        assertEquals(expectedFilmDto.getDescription(), actualFilm.getDescription());
        assertEquals(expectedFilmDto.getYear(), actualFilm.getYear());
        assertEquals(expectedFilmDto.getMinimalAge(), actualFilm.getMinimalAge());
        assertEquals(expectedFilmDto.getDurationInMinutes(), actualFilm.getDurationInMinutes());
        assertEquals(expectedFilmDto.getGenre(), actualFilm.getGenre());
        assertEquals(expectedFilmDto.getPosterPath(), actualFilm.getPosterPath());
        assertEquals(expectedFilmDto.getFileId(), actualFilm.getFileId());
    }

    @Test
    void whenGetByIdAndFilmNotExistsThenReturnEmptyOptional() {
        // Arrange
        when(filmRepository.getById(2)).thenReturn(Optional.empty());

        // Act
        Optional<FilmDto> result = simpleFilmService.getById(2);

        // Assert
        assertTrue(result.isEmpty());
    }
}
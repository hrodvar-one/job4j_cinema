package ru.job4j.cinema.service.film;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.file.FileRepository;
import ru.job4j.cinema.repository.film.FilmRepository;
import ru.job4j.cinema.repository.genre.GenreRepository;

import java.util.List;

@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FileRepository fileRepository;

    public SimpleFilmService(FilmRepository filmRepository, GenreRepository genreRepository, FileRepository fileRepository) {
        this.filmRepository = filmRepository;
        this.genreRepository = genreRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public List<FilmDto> getAll() {
        List<Film> films = filmRepository.getAll();
        return films.stream()
                .map(film -> {
                    String genre = genreRepository.getById(film.getGenreId())
                            .map(Genre::getName)
                            .orElse("Unknown");
                    String posterPath = fileRepository.getById(film.getFileId())
                            .map(File::getPath)
                            .orElse("");
                    return new FilmDto(
                            film.getId(),
                            film.getName(),
                            film.getDescription(),
                            film.getYear(),
                            film.getMinimalAge(),
                            film.getDurationInMinutes(),
                            genre,
                            posterPath,
                            film.getFileId()
                    );
                })
                .toList();
    }
}

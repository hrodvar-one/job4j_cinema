package ru.job4j.cinema.repository.file;

import ru.job4j.cinema.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository {

    List<File> getAll();

    Optional<File> getById(int id);
}

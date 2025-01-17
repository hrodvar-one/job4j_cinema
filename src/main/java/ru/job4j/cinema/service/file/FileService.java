package ru.job4j.cinema.service.file;

import ru.job4j.cinema.dto.FileDto;

import java.util.Optional;

public interface FileService {

    Optional<FileDto> getById(int id);
}

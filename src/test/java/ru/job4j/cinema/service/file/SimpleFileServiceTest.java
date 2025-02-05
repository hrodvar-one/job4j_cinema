package ru.job4j.cinema.service.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.file.FileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleFileServiceTest {

    @Mock
    private FileRepository fileRepository;

    private SimpleFileService simpleFileService;

    private final String testStorageDirectory = "test_storage";

    @BeforeEach
    void setUp() {
        simpleFileService = new SimpleFileService(fileRepository, testStorageDirectory);
    }

    @Test
    void whenGetByIdThenReturnFileDto() throws IOException {
        // Arrange
        int fileId = 1;
        String fileName = "test.txt";
        String filePath = testStorageDirectory + "/test.txt";
        byte[] fileContent = "Test content".getBytes();

        File file = new File(fileId, fileName, filePath);
        when(fileRepository.getById(fileId)).thenReturn(Optional.of(file));
        Files.write(Path.of(filePath), fileContent); // Записываем временный файл для теста

        // Act
        Optional<FileDto> result = simpleFileService.getById(fileId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(fileName, result.get().getName());
        assertArrayEquals(fileContent, result.get().getFileContent());
    }

    @Test
    void whenGetByIdAndFileNotExistsThenReturnEmptyOptional() {
        // Arrange
        int fileId = 2;
        when(fileRepository.getById(fileId)).thenReturn(Optional.empty());

        // Act
        Optional<FileDto> result = simpleFileService.getById(fileId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void whenCreateStorageDirectoryThenNoExceptionThrown() {
        // Act & Assert
        assertDoesNotThrow(() -> new SimpleFileService(fileRepository, testStorageDirectory));
    }

    @Test
    void whenCreateStorageDirectoryFailsThenThrowRuntimeException() throws IOException {
        // Arrange: Очистка папки перед тестом
        Path storagePath = Path.of(testStorageDirectory);

        // Удаляем все файлы внутри папки перед тем, как пытаться её удалить
        if (Files.exists(storagePath) && Files.isDirectory(storagePath)) {
            Files.walk(storagePath)
                    .sorted(Comparator.reverseOrder()) // Сначала удаляем файлы, затем директорию
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }

        // Создаём **файл вместо папки**, чтобы вызвать ошибку
        Files.createFile(storagePath);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> new SimpleFileService(fileRepository, testStorageDirectory));

        // Cleanup: Удаляем файл, чтобы он не мешал другим тестам
        Files.deleteIfExists(storagePath);
    }

    @Test
    void whenReadFileAsBytesThenReturnContent() throws IOException {
        // Arrange
        int fileId = 1;
        String fileName = "test.txt";
        String filePath = testStorageDirectory + "/test.txt";
        byte[] expectedContent = "Test content".getBytes();

        File file = new File(fileId, fileName, filePath);

        when(fileRepository.getById(fileId)).thenReturn(Optional.of(file));
        Files.write(Path.of(filePath), expectedContent); // Записываем временный файл для теста

        // Act
        Optional<FileDto> result = simpleFileService.getById(fileId);

        // Assert
        assertTrue(result.isPresent());
        assertArrayEquals(expectedContent, result.get().getFileContent());
    }

    @Test
    void whenReadFileAsBytesFailsThenThrowRuntimeException() {
        // Arrange
        String invalidPath = "non_existent_file.txt";
        int fileId = 999;
        File file = new File(fileId, "Invalid File", invalidPath);

        when(fileRepository.getById(fileId)).thenReturn(Optional.of(file));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> simpleFileService.getById(fileId));
    }
}
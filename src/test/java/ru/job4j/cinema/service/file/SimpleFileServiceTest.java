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

        int fileId = 1;
        String fileName = "test.txt";
        String filePath = testStorageDirectory + "/test.txt";
        byte[] fileContent = "Test content".getBytes();

        File file = new File(fileId, fileName, filePath);
        when(fileRepository.getById(fileId)).thenReturn(Optional.of(file));
        Files.write(Path.of(filePath), fileContent);

        Optional<FileDto> result = simpleFileService.getById(fileId);

        assertTrue(result.isPresent());
        assertEquals(fileName, result.get().getName());
        assertArrayEquals(fileContent, result.get().getFileContent());
    }

    @Test
    void whenGetByIdAndFileNotExistsThenReturnEmptyOptional() {

        int fileId = 2;
        when(fileRepository.getById(fileId)).thenReturn(Optional.empty());

        Optional<FileDto> result = simpleFileService.getById(fileId);

        assertTrue(result.isEmpty());
    }

    @Test
    void whenCreateStorageDirectoryThenNoExceptionThrown() {

        assertDoesNotThrow(() -> new SimpleFileService(fileRepository, testStorageDirectory));
    }

    @Test
    void whenCreateStorageDirectoryFailsThenThrowRuntimeException() throws IOException {

        Path storagePath = Path.of(testStorageDirectory);

        if (Files.exists(storagePath) && Files.isDirectory(storagePath)) {
            Files.walk(storagePath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        }

        Files.createFile(storagePath);

        assertThrows(RuntimeException.class, () -> new SimpleFileService(fileRepository, testStorageDirectory));

        Files.deleteIfExists(storagePath);
    }

    @Test
    void whenReadFileAsBytesThenReturnContent() throws IOException {

        int fileId = 1;
        String fileName = "test.txt";
        String filePath = testStorageDirectory + "/test.txt";
        byte[] expectedContent = "Test content".getBytes();

        File file = new File(fileId, fileName, filePath);

        when(fileRepository.getById(fileId)).thenReturn(Optional.of(file));
        Files.write(Path.of(filePath), expectedContent);

        Optional<FileDto> result = simpleFileService.getById(fileId);

        assertTrue(result.isPresent());
        assertArrayEquals(expectedContent, result.get().getFileContent());
    }

    @Test
    void whenReadFileAsBytesFailsThenThrowRuntimeException() {

        String invalidPath = "non_existent_file.txt";
        int fileId = 999;
        File file = new File(fileId, "Invalid File", invalidPath);

        when(fileRepository.getById(fileId)).thenReturn(Optional.of(file));

        assertThrows(RuntimeException.class, () -> simpleFileService.getById(fileId));
    }
}
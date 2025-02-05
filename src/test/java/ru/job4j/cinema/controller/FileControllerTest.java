package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.job4j.cinema.dto.FileDto;
import ru.job4j.cinema.service.file.FileService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileControllerTest {

    @Mock
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    @Test
    void whenFileExistsThenReturnFileContent() {

        int fileId = 1;
        byte[] fileContent = "test content".getBytes();
        FileDto fileDto = new FileDto("test.txt", fileContent);

        when(fileService.getById(fileId)).thenReturn(Optional.of(fileDto));

        ResponseEntity<?> response = fileController.getById(fileId);

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(fileContent, (byte[]) response.getBody());
        verify(fileService, times(1)).getById(fileId);
    }

    @Test
    void whenFileDoesNotExistThenReturnNotFound() {

        int fileId = 1;
        when(fileService.getById(fileId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = fileController.getById(fileId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(fileService, times(1)).getById(fileId);
    }
}
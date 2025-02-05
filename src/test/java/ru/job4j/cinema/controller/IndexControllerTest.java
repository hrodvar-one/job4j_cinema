package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @InjectMocks
    private IndexController indexController;

    @Test
    void whenGetIndexThenReturnIndexView() {
        // Act
        String viewName = indexController.getIndex();

        // Assert
        assertEquals("index", viewName);
    }

    @Test
    void whenGetIndexThenViewShouldNotBeNullOrUnexpected() {
        // Act
        String viewName = indexController.getIndex();

        // Assert
        assertNotNull(viewName);
        assertNotEquals("wrongView", viewName);
    }
}
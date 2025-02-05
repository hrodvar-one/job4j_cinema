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

        String viewName = indexController.getIndex();

        assertEquals("index", viewName);
    }

    @Test
    void whenGetIndexThenViewShouldNotBeNullOrUnexpected() {

        String viewName = indexController.getIndex();

        assertNotNull(viewName);
        assertNotEquals("wrongView", viewName);
    }
}
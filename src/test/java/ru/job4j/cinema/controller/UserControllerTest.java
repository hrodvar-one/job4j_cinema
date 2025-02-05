package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserController userController;

    @Test
    void whenGetRegistrationPageThenReturnRegisterView() {
        // Act
        String viewName = userController.getRegistrationPage();

        // Assert
        assertEquals("users/register", viewName);
    }

    @Test
    void whenRegisterSuccessfullyThenRedirectToFilms() {
        // Arrange
        User user = new User(1, "John Doe", "john@example.com", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        // Act
        String viewName = userController.register(model, user);

        // Assert
        assertEquals("redirect:/films", viewName);
        verify(userService, times(1)).save(user);
    }

    @Test
    void whenRegisterFailsThenReturnError404() {
        // Arrange
        User user = new User(1, "John Doe", "john@example.com", "password");
        when(userService.save(user)).thenReturn(Optional.empty());

        // Act
        String viewName = userController.register(model, user);

        // Assert
        assertEquals("errors/404", viewName);
        verify(model, times(1)).addAttribute("message",
                "Пользователь с такой почтой уже существует");
    }

    @Test
    void whenGetLoginPageThenReturnLoginView() {
        // Act
        String viewName = userController.getLoginPage();

        // Assert
        assertEquals("users/login", viewName);
    }

    @Test
    void whenLoginSuccessfullyThenRedirectToFilms() {
        // Arrange
        User user = new User(1, "John Doe", "john@example.com", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);

        // Act
        String viewName = userController.loginUser(user, model, request);

        // Assert
        assertEquals("redirect:/films", viewName);
        verify(session, times(1)).setAttribute("user", user);
    }

    @Test
    void whenLoginFailsThenReturnLoginViewWithErrorMessage() {
        // Arrange
        User user = new User(1, "John Doe", "john@example.com", "wrongpassword");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

        // Act
        String viewName = userController.loginUser(user, model, request);

        // Assert
        assertEquals("users/login", viewName);
        verify(model, times(1)).addAttribute("error",
                "Почта или пароль введены неверно");
    }

    @Test
    void whenLogoutThenRedirectToLogin() {
        // Arrange
        doNothing().when(session).invalidate();

        // Act
        String viewName = userController.logout(session);

        // Assert
        assertEquals("redirect:/users/login", viewName);
        verify(session, times(1)).invalidate();
    }
}
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

        String viewName = userController.getRegistrationPage();

        assertEquals("users/register", viewName);
    }

    @Test
    void whenRegisterSuccessfullyThenRedirectToFilms() {

        User user = new User(1, "John Doe", "john@example.com", "password");
        when(userService.save(user)).thenReturn(Optional.of(user));

        String viewName = userController.register(model, user);

        assertEquals("redirect:/films", viewName);
        verify(userService, times(1)).save(user);
    }

    @Test
    void whenRegisterFailsThenReturnError404() {

        User user = new User(1, "John Doe", "john@example.com", "password");
        when(userService.save(user)).thenReturn(Optional.empty());

        String viewName = userController.register(model, user);

        assertEquals("errors/404", viewName);
        verify(model, times(1)).addAttribute("message",
                "Пользователь с такой почтой уже существует");
    }

    @Test
    void whenGetLoginPageThenReturnLoginView() {

        String viewName = userController.getLoginPage();

        assertEquals("users/login", viewName);
    }

    @Test
    void whenLoginSuccessfullyThenRedirectToFilms() {

        User user = new User(1, "John Doe", "john@example.com", "password");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(user));
        when(request.getSession()).thenReturn(session);

        String viewName = userController.loginUser(user, model, request);

        assertEquals("redirect:/films", viewName);
        verify(session, times(1)).setAttribute("user", user);
    }

    @Test
    void whenLoginFailsThenReturnLoginViewWithErrorMessage() {

        User user = new User(1, "John Doe", "john@example.com", "wrongpassword");
        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

        String viewName = userController.loginUser(user, model, request);

        assertEquals("users/login", viewName);
        verify(model, times(1)).addAttribute("error",
                "Почта или пароль введены неверно");
    }

    @Test
    void whenLogoutThenRedirectToLogin() {

        doNothing().when(session).invalidate();

        String viewName = userController.logout(session);

        assertEquals("redirect:/users/login", viewName);
        verify(session, times(1)).invalidate();
    }
}
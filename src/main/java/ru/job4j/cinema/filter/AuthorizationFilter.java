package ru.job4j.cinema.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
@Order(1)
public class AuthorizationFilter extends HttpFilter {

    // URL, требующий авторизации
    private static final Set<String> PROTECTED_URLS = Set.of("/tickets/buy");

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        var uri = request.getRequestURI();

        // Проверяем, нужно ли запрашивать авторизацию
        if (requiresAuthorization(uri)) {
            var userLoggedIn = request.getSession().getAttribute("user") != null;
            if (!userLoggedIn) {
                var loginPageUrl = request.getContextPath() + "/users/login";
                response.sendRedirect(loginPageUrl);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean requiresAuthorization(String uri) {
        return PROTECTED_URLS.contains(uri);
    }
}

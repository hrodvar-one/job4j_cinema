//package ru.job4j.cinema.filter;
//
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//@Order(1)
//public class AuthorizationFilter extends HttpFilter {
//
//    @Override
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        var uri = request.getRequestURI();
//        if (isAlwaysPermitted(uri)) {
//            chain.doFilter(request, response);
//            return;
//        }
//        var userLoggedIn = request.getSession().getAttribute("user") != null;
//        if (!userLoggedIn) {
//            var loginPageUrl = request.getContextPath() + "/users/login";
//            response.sendRedirect(loginPageUrl);
//            return;
//        }
//        chain.doFilter(request, response);
//    }
//
//    private boolean isAlwaysPermitted(String uri) {
//        return uri.startsWith("/users/register")
//                || uri.startsWith("/users/login")
//                || uri.startsWith("/js")
//                || uri.startsWith("/css");
//    }
//}

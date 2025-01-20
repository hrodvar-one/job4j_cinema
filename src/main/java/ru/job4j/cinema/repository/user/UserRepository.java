package ru.job4j.cinema.repository.user;

import ru.job4j.cinema.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> save(User user);
}

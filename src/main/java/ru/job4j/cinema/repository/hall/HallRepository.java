package ru.job4j.cinema.repository.hall;

import ru.job4j.cinema.model.Hall;

import java.util.Optional;

public interface HallRepository {

    Optional<Hall> getById(int id);
}

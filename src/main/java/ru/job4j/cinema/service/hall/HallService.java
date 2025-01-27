package ru.job4j.cinema.service.hall;

import ru.job4j.cinema.model.Hall;

import java.util.Optional;

public interface HallService {

    Optional<Hall> getById(int id);
}

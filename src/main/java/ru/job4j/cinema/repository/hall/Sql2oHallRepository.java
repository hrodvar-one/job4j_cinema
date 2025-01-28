package ru.job4j.cinema.repository.hall;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Hall;

import java.util.Optional;

@Repository
public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> getById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * FROM halls WHERE id = :id");
            query.addParameter("id", id);
            Hall hall = query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(hall);
        }
    }

//    @Override
//    public Optional<Hall> getById(int id) {
//        try (Connection connection = sql2o.open()) {
//            // SQL-запрос с подзапросом для извлечения halls_id из film_sessions
//            Query query = connection.createQuery(
//                    "SELECT * FROM halls WHERE id = ("
//                            + "   SELECT halls_id FROM film_sessions WHERE id = :id"
//                            + ")"
//            );
//            query.addParameter("id", id);
//            Hall hall = query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetchFirst(Hall.class);
//            return Optional.ofNullable(hall);
//        }
//    }
}

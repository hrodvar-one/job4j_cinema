package ru.job4j.cinema.repository.genre;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Genre> getAll() {
        try (Connection con = sql2o.open()) {
            Query query = con.createQuery("SELECT * FROM genres");
            return query.setColumnMappings(Genre.COLUMN_MAPPING).executeAndFetch(Genre.class);
        }
    }

    @Override
    public Optional<Genre> getById(int id) {
        try (Connection con = sql2o.open()) {
            Query query = con.createQuery("SELECT * FROM genres WHERE id = :id");
            query.addParameter("id", id);
            Genre genre = query.setColumnMappings(Genre.COLUMN_MAPPING).executeAndFetchFirst(Genre.class);
            return Optional.ofNullable(genre);
        }
    }
}

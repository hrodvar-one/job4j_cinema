package ru.job4j.cinema.repository.file;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.List;
import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<File> getAll() {
        String sql = "SELECT * FROM files";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(File.class);
        }
    }

    @Override
    public Optional<File> getById(int id) {
        try (Connection connection = sql2o.open()) {
            Query query = connection.createQuery("SELECT * FROM files WHERE id = :id");
            query.addParameter("id", id);
            File file = query.setColumnMappings(File.COLUMN_MAPPING).executeAndFetchFirst(File.class);
            return Optional.ofNullable(file);
        }
    }
}

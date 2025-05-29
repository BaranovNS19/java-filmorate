package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRowMappers implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa raiting = new Mpa();
        raiting.setId(resultSet.getLong("id"));
        raiting.setName(resultSet.getString("name"));
        return raiting;
    }
}

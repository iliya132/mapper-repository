package ru.iliya132.mapper.repository.impl;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.iliya132.mapper.helper.Mapper;

import java.sql.PreparedStatement;
import java.util.ConcurrentModificationException;
import java.util.List;

public class ProcessingHelper<T, V> {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final Mapper<T, V> mapper;

    public ProcessingHelper(NamedParameterJdbcTemplate jdbcTemplate, Mapper<T, V> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    public List<V> processUpdates(String sql, List<V> values) {
        return jdbcTemplate.getJdbcOperations().execute((ConnectionCallback<List<V>>) con -> {
            try (PreparedStatement statement = con.prepareStatement(sql)) {
                PreparedStatementSetter<T, V> preparedStatementSetter = new PreparedStatementSetter<>(statement);
                mapper.setToStatement(preparedStatementSetter, values);
                int updated = statement.executeUpdate();
                if (updated != values.size()) {
                    throw new ConcurrentModificationException("updated count doesn't match values size");
                }
                var generatedKeys = statement.getGeneratedKeys();
                mapper.updateKeys(values, generatedKeys);
            } catch (Exception e) {
                throw e;
            }
            return List.of();
        });
    }

}

package ru.iliya132.mapper.helper.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.iliya132.mapper.helper.Mapper;
import ru.iliya132.mapper.repository.impl.PreparedStatementSetter;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FieldMapper<TKey, TValue> implements Mapper<TKey, TValue> {

    @Override
    public RowMapper<TValue> rowMapper() {
        return null;
    }

    @Override
    public SqlParameterSource[] paramsForKey(Collection<TKey> tKeys) {
        return null;
    }

    @Override
    public String whereForKey(Collection<TKey> tKeys) {
        return null;
    }

    @Override
    public Map<String, Object> getKeyAsParams(TKey tKey) {
        return null;
    }

    @Override
    public Map<String, List<Object>> getKeysAsParams(Collection<TKey> tKeys) {
        return null;
    }

    @Override
    public List<String> getColumns() {
        return null;
    }

    @Override
    public List<String> getKeyColumns() {
        return null;
    }

    @Override
    public void setToStatement(PreparedStatementSetter<TKey, TValue> preparedStatementSetter, List<TValue> tValues) {

    }

    @Override
    public void updateKeys(List<TValue> tValues, ResultSet generatedKeys) {

    }
}

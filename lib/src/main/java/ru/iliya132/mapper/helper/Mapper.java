package ru.iliya132.mapper.helper;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ru.iliya132.mapper.repository.impl.PreparedStatementSetter;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Mapper<TKey, TValue> {
    RowMapper<TValue> rowMapper();

    SqlParameterSource[] paramsForKey(Collection<TKey> tKeys);

    String whereForKey(Collection<TKey> tKeys);

    Map<String, Object> getKeyAsParams(TKey tKey);

    Map<String, List<Object>> getKeysAsParams(Collection<TKey> tKeys);

    List<String> getColumns();

    List<String> getKeyColumns();

    void setToStatement(PreparedStatementSetter<TKey, TValue> preparedStatementSetter, List<TValue> values);

    void updateKeys(List<TValue> values, ResultSet generatedKeys);
}

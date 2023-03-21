package ru.iliya132.mapper.helper.impl;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.NonNull;
import ru.iliya132.mapper.helper.Mapper;
import ru.iliya132.mapper.helper.types.ColumnInfo;
import ru.iliya132.mapper.helper.types.annotations.Column;
import ru.iliya132.mapper.helper.types.annotations.Key;
import ru.iliya132.mapper.repository.impl.PreparedStatementSetter;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class ReflectiveMapper<TKey, TValue> implements Mapper<TKey, TValue> {

    private final Class<TKey> keyClass;
    private final Class<TValue> valueClass;
    private final List<ColumnInfo> keyColumns = new ArrayList<>();
    private final List<ColumnInfo> columns = new ArrayList<>();

    public ReflectiveMapper(Class<TValue> valueClass, Class<TKey> keyClass) {
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        reflectColumns();
    }

    private void reflectColumns() {
        var entityFields = valueClass.getFields();
        for (var field : entityFields) {
            var columnInfo = reflectColumnInfo(field);
            columns.add(columnInfo);
            if (field.isAnnotationPresent(Key.class)) {
                keyColumns.add(columnInfo);
            }
        }
    }

    private ColumnInfo reflectColumnInfo(Field field) {
        var columnInfo = new ColumnInfo();
        columnInfo.setFieldName(field.getName());
        columnInfo.setField(field);
        columnInfo.setIsKey(field.isAnnotationPresent(Key.class));
        columnInfo.setSqlName(parseSqlName(field));
        columnInfo.setGetter(object -> {
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        columnInfo.setSetter((object, value) -> {
            try {
                field.setAccessible(true);
                field.set(object, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return columnInfo;
    }

    private String parseSqlName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).name();
        }
        return toSnakeCase(field.getName());
    }

    private String toSnakeCase(String text) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return text.replaceAll(regex, replacement)
                .toLowerCase();
    }

    @Override
    public RowMapper<TValue> rowMapper() {
        return (rs, rowNum) -> newEntityFromSet(rs);
    }

    private TValue newEntityFromSet(ResultSet rs) {
        TValue result = createInstance();
        columns.forEach(col -> {
            try {
                Object sqlColumnValue = rs.getObject(col.getSqlName(), col.getField().getType());
                col.getSetter().accept(result, sqlColumnValue);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    private TValue createInstance() {
        try {
            return valueClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SqlParameterSource[] paramsForKey(Collection<TKey> tKeys) {
        SqlParameterSource[] parameters = new SqlParameterSource[tKeys.size()];
        int i = 0;
        for (var key : tKeys) {
            var batch = new HashMap<String, Object>();
            for (var col : keyColumns) {
                batch.put(col.getSqlName(), col.getGetter().apply(key));
            }
            parameters[i++] = castToSqlParameterSource(batch);
        }
        return parameters;
    }

    private SqlParameterSource castToSqlParameterSource(HashMap<String, Object> batch) {
        return new SqlParameterSource() {
            @Override
            public boolean hasValue(@NonNull String paramName) {
                return batch.containsKey(paramName);
            }

            @Override
            public Object getValue(@NonNull String paramName) throws IllegalArgumentException {
                return batch.get(paramName);
            }
        };
    }


    @Override
    public String whereForKey(Collection<TKey> tKeys) {
        return keyColumns.stream()
                        .map(it -> "%s = :%s".formatted(it.getSqlName(), it.getSqlName()))
                        .collect(Collectors.joining(" AND "));
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

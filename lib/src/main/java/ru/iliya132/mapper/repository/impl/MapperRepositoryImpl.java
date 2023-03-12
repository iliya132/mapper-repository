package ru.iliya132.mapper.repository.impl;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import ru.iliya132.mapper.DatabaseType;
import ru.iliya132.mapper.helper.Filter;
import ru.iliya132.mapper.helper.Mapper;
import ru.iliya132.mapper.helper.Utils;
import ru.iliya132.mapper.helper.impl.ReflectiveMapper;
import ru.iliya132.mapper.query.QueryBuilder;
import ru.iliya132.mapper.repository.MapperRepository;

import java.util.List;
import java.util.Optional;

public class MapperRepositoryImpl<TKey, TValue> implements MapperRepository<TKey, TValue> {

    private final Mapper<TValue> mapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final QueryBuilder queryBuilder;

    public MapperRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                DatabaseType dbType) {
        this.queryBuilder = Utils.getQueryBuilder(dbType);
        this.mapper = new ReflectiveMapper<>();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public MapperRepositoryImpl(
            Mapper<TValue> mapper,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            DatabaseType dbType) {
        this.mapper = mapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.queryBuilder = Utils.getQueryBuilder(dbType);
    }

    @Override
    public Optional<TValue> findById(TKey tKey) {
        return Optional.empty();
    }

    @NonNull
    @Override
    public List<TValue> findByIds(Iterable<TKey> tKeys) {
        return null;
    }

    @NonNull
    @Override
    public List<TValue> findByFilter(Filter<TValue> filter) {
        return null;
    }

    @Override
    public Integer update(Iterable<TValue> records) {
        return null;
    }

    @Override
    public Integer delete(Iterable<TKey> tKeys) {
        return null;
    }


}

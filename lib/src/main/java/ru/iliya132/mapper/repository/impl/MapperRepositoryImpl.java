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
import ru.iliya132.mapper.types.TableConfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapperRepositoryImpl<TKey, TValue> implements MapperRepository<TKey, TValue> {

    private final Mapper<TKey, TValue> mapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final DatabaseType dbType;
    private final TableConfig<TValue> tableConfig;

    public MapperRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                DatabaseType dbType,
                                Class<TValue> valueClass,
                                Class<TKey> keyClass) {
        this.dbType = dbType;
        this.tableConfig = TableConfig.from(valueClass);
        this.mapper = new ReflectiveMapper<>(valueClass, keyClass);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public MapperRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                                DatabaseType dbType,
                                TableConfig<TValue> tableConfig,
                                Class<TKey> keyClass,
                                Class<TValue> valueClass) {
        this.dbType = dbType;
        this.tableConfig = tableConfig;
        this.mapper = new ReflectiveMapper<>(valueClass, keyClass);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public MapperRepositoryImpl(
            Mapper<TKey, TValue> mapper,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            DatabaseType dbType,
            TableConfig<TValue> tableConfig) {
        this.mapper = mapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.dbType = dbType;
        this.tableConfig = tableConfig;
    }

    @Override
    public Optional<TValue> findById(TKey tKey) {
        Map<String, Object> keyParams = mapper.getKeyAsParams(tKey);
        Filter filter = new Filter(keyParams);
        return findSingle(filter);
    }

    @NonNull
    @Override
    public List<TValue> findByIds(Iterable<TKey> tKeys) {
        Map<String, List<Object>> params = mapper.getKeysAsParams(tKeys);
        Filter filter = new Filter();
        filter.addRepeatedParameters(params);
        return findMany(filter);
    }

    @NonNull
    @Override
    public List<TValue> findByFilter(Filter filter) {
        return findMany(filter);
    }

    @Override
    public TValue update(Iterable<TValue> records) {

        return null;
    }

    @Override
    public Integer delete(Collection<TKey> tKeys) {
        String whereForKey = mapper.whereForKey(tKeys);
        String sql = getQueryBuilder()
                .delete()
                .setTableName(tableConfig.getTableName())
                .setWhere(whereForKey)
                .build();
        namedParameterJdbcTemplate.batchUpdate(sql, mapper.paramsForKey(tKeys));
        return null;
    }

    private Optional<TValue> findSingle(Filter filter) {
        String sql = getQueryBuilder()
                .select()
                .setFrom(tableConfig.getTableName(), "a")
                .setWhere(filter.getWhere())
                .setLimit(1)
                .build();
        return ofNullable(namedParameterJdbcTemplate.queryForObject(sql, filter.getParams(), mapper.rowMapper()));
    }

    private List<TValue> findMany(Filter filter) {
        String sql = getQueryBuilder()
                .select()
                .setFrom(tableConfig.getTableName(), "a")
                .setWhere(filter.getWhere())
                .build();
        return namedParameterJdbcTemplate.query(sql, filter.getParams(), mapper.rowMapper());
    }

    private List<TValue> update(List<TValue> records){
        List<String> keyColumns = mapper.getKeyColumns();
        String where = buildWhereForKeys(keyColumns);
        String sql = getQueryBuilder()
                .update()
                .setTableName(tableConfig.getTableName())
                .setColumns(mapper.getColumns())
                .setWhere(where)
                .build();
        return null;

    }

    private String buildWhereForKeys(List<String> keys){
        return keys.stream().map(key -> "%s=:%s".formatted(key, key))
                .collect(Collectors.joining(" AND "));
    }

    private QueryBuilder getQueryBuilder() {
        return Utils.getQueryBuilder(dbType);
    }

    private static <T> Optional<T> ofNullable(T value){
        return Optional.ofNullable(value);
    }
}

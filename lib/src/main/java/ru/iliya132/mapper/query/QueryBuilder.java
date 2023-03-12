package ru.iliya132.mapper.query;

import java.util.List;

public interface QueryBuilder {
    QueryBuilder setTitle(String title);
    QueryBuilder select();
    QueryBuilder update();
    QueryBuilder delete();
    QueryBuilder setColumns(List<String> columns);

    QueryBuilder setFrom(String from, String alias);
    QueryBuilder appendJoin(String join);
    QueryBuilder setWhere(String where);
    QueryBuilder setLimit(int limit);
    QueryBuilder setOrderBy(String orderBy);
    QueryBuilder groupBy(String groupBy);
    QueryBuilder having(String having);
    QueryBuilder setTableName(String tableName);
    String build();
}

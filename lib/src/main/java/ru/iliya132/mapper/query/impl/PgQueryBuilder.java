package ru.iliya132.mapper.query.impl;

import ru.iliya132.mapper.exceptions.InvalidOperationException;
import ru.iliya132.mapper.query.QueryBuilder;
import ru.iliya132.mapper.types.SqlOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PgQueryBuilder implements QueryBuilder {
    private PgQueryBuilder() {
    }

    private SqlOperation operation = SqlOperation.SELECT;
    private StringBuilder builder;
    private List<String> columns;
    private String from;
    private String fromAlias;
    private final List<String> joins = new ArrayList<>();
    private String where;
    private Integer limit;
    private String orderBy;
    private String groupBy;
    private String having;
    private String title;


    @Override
    public QueryBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    @Override
    public QueryBuilder select() {
        this.operation = SqlOperation.SELECT;
        return this;
    }

    @Override
    public QueryBuilder update() {
        this.operation = SqlOperation.UPDATE;
        return this;
    }

    @Override
    public QueryBuilder delete() {
        this.operation = SqlOperation.DELETE;
        return this;
    }

    @Override
    public QueryBuilder setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public QueryBuilder setFrom(String from, String alias) {
        this.from = from;
        this.fromAlias = alias;
        return this;
    }

    @Override
    public QueryBuilder appendJoin(String join) {
        this.joins.add(join);
        return this;
    }

    @Override
    public QueryBuilder setWhere(String where) {
        this.where = where;
        return this;
    }

    @Override
    public QueryBuilder setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public QueryBuilder setTableName(String tableName) {
        this.from = tableName;
        return this;
    }

    @Override
    public QueryBuilder setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public QueryBuilder groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    @Override
    public QueryBuilder having(String having) {
        this.having = having;
        return this;
    }

    @Override
    public String build() {
        validate();
        builder = new StringBuilder();
        return switch (operation) {
            case SELECT -> buildSelectQuery();
            case UPDATE -> buildUpdateQuery();
            case DELETE -> buildDeleteQuery();
        };
    }

    private String buildSelectQuery() {
        appendTitle();
        appendOperation();
        appendColumns();
        appendFrom();
        appendJoinQuery();
        appendWhere();
        appendGroupBy();
        appendHavingQuery();
        appendOrderBy();
        appendLimit();
        appendSemicolon();
        return builder.toString();
    }

    private void appendSemicolon() {
        append(";");
    }

    private String buildUpdateQuery() {
        appendTitle();
        appendOperation();
        appendTableName();
        appendLine();
        appendUpdateSet();
        appendWhere();
        appendSemicolon();
        return builder.toString();
    }

    private String buildDeleteQuery() {
        appendTitle();
        appendOperation();
        appendFrom();
        appendWhere();
        appendSemicolon();
        return builder.toString();
    }

    private void appendTableName() {
        append(from);
    }

    private void appendUpdateSet() {
        append("SET ");
        append(columns.stream()
                .map(it -> "%s=:%s".formatted(it, it))
                .collect(Collectors.joining(", ")));
        appendLine();
    }

    private void appendTitle() {
        doIfNotEmpty(title, title -> appendLine("--", title));
    }

    private void appendOperation() {
        append(operation.toString(), " ");
    }

    private void appendColumns() {
        doIfNotNull(columns, columns -> {
            var columnsStr = String.join(", ", columns);
            doIfNotEmpty(columnsStr, this::appendLine);
        });
    }

    private void appendFrom() {
        appendLine("FROM ", from, " as ", this.fromAlias);
    }

    private void appendWhere() {
        doIfNotEmpty(where, where -> appendLine("WHERE ", where));
    }

    private void appendGroupBy() {
        doIfNotEmpty(groupBy, groupBy -> appendLine("GROUP BY ", groupBy));
    }

    private void appendOrderBy() {
        doIfNotEmpty(orderBy, str -> appendLine("ORDER BY ", str));
    }

    private void appendLimit() {
        doIfNotNull(this.limit, limit -> appendLine("LIMIT ", this.limit.toString()));
    }

    private void appendJoinQuery() {
        if (!joins.isEmpty()) {
            for (String join : joins) {
                doIfNotEmpty(join, this::appendLine);
            }
        }
    }

    private void appendHavingQuery() {
        doIfNotEmpty(having, str -> appendLine("HAVING ", str));
    }

    private <T> void doIfNotNull(T obj, Consumer<T> consumer) {
        if (obj != null) {
            consumer.accept(obj);
        }
    }

    private void doIfNotEmpty(String toCheck, Consumer<String> consumer) {
        if (toCheck != null && !toCheck.isEmpty()) {
            consumer.accept(toCheck);
        }
    }

    private void appendLine(String... lines) {
        append(lines);
        builder.append("\n");
    }

    private void append(String... lines) {
        for (String str : lines) {
            builder.append(str);
        }
    }

    private void validate() {
        if (operation.equals(SqlOperation.DELETE) && columns != null && !columns.isEmpty()) {
            throw new InvalidOperationException("Can't set columns when delete operation is set");
        }

        if (from == null || from.isEmpty()) {
            throw new InvalidOperationException("From clause can't be null or empty");
        }
    }

    public static PgQueryBuilder start() {
        return new PgQueryBuilder();
    }
}

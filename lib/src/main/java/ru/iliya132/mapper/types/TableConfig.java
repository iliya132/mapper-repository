package ru.iliya132.mapper.types;

import lombok.Getter;

@Getter
public class TableConfig<TValue> {
    private final String tableName;

    public TableConfig(String tableName) {

        this.tableName = tableName;
    }

    public static <TValue> TableConfig<TValue> from(Class<TValue> type) {
        return null;
    }
}

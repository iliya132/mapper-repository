package ru.iliya132.mapper.helper;

import ru.iliya132.mapper.DatabaseType;
import ru.iliya132.mapper.query.QueryBuilder;
import ru.iliya132.mapper.query.impl.PgQueryBuilder;

public class Utils {
    public static QueryBuilder getQueryBuilder(DatabaseType dbType) {
        return switch (dbType) {
            case POSTGRESQL -> PgQueryBuilder.start();
        };
    }
}

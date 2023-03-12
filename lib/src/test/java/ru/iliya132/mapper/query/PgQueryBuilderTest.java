package ru.iliya132.mapper.query;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.iliya132.mapper.query.impl.PgQueryBuilder;

import java.util.List;

public class PgQueryBuilderTest {
    private static final String TABLE_NAME = "db.test_table";
    private static final String JOIN_NAME = "join db.test_joined_table as b on a.first_column = b.first_column";
    private static final String FIRST_COLUMN = "first_column";
    private static final String SECOND_COLUMN = "second_column";
    private static final String THIRD_COLUMN = "third_column";

    @Test
    public void canBuildSelectQuery() {
        String sql = PgQueryBuilder.start()
                .select()
                .setTitle("testing that PgQueryBuilder can build select queries")
                .setFrom(TABLE_NAME, "a")
                .setColumns(List.of(FIRST_COLUMN, SECOND_COLUMN, THIRD_COLUMN))
                .appendJoin(JOIN_NAME)
                .setWhere("a.%s = b.%s".formatted(FIRST_COLUMN, FIRST_COLUMN))
                .setLimit(5)
                .setOrderBy("a.%s".formatted(SECOND_COLUMN))
                .build();
        String expectedSql =
                """
                        --testing that PgQueryBuilder can build select queries
                        SELECT first_column, second_column, third_column
                        FROM db.test_table as a
                        join db.test_joined_table as b on a.first_column = b.first_column
                        WHERE a.first_column = b.first_column
                        ORDER BY a.second_column
                        LIMIT 5
                        ;""";
        Assertions.assertThat(sql).isEqualTo(expectedSql);
    }

    @Test
    public void canBuildUpdateQuery() {
        String sql = PgQueryBuilder.start()
                .update()
                .setTableName(TABLE_NAME)
                .setColumns(List.of(FIRST_COLUMN, THIRD_COLUMN))
                .setWhere("%s = :some_value".formatted(FIRST_COLUMN))
                .build();
        String expectedSql = """
                UPDATE db.test_table
                SET first_column=:first_column, third_column=:third_column
                WHERE first_column = :some_value
                ;""";
        Assertions.assertThat(sql).isEqualTo(expectedSql);
    }

    @Test
    public void canBuildDeleteQuery() {
        String sql = PgQueryBuilder.start()
                .delete()
                .setFrom(TABLE_NAME, "a")
                .setWhere("%s = :some_value".formatted(FIRST_COLUMN))
                .build();
        String expectedSql = """
                DELETE FROM db.test_table as a
                WHERE first_column = :some_value
                ;""";
        Assertions.assertThat(sql).isEqualTo(expectedSql);
    }
}

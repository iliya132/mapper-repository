package ru.iliya132.mapper.helper.types;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Getter
@Setter
public class ColumnInfo {
    private String sqlName;
    private String fieldName;
    private Field field;
    private Boolean isKey;
    private Function<Object, Object> getter;
    private BiConsumer<Object, Object> setter;
}

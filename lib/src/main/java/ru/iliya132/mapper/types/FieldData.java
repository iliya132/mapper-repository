package ru.iliya132.mapper.types;

import lombok.AllArgsConstructor;

import java.lang.invoke.MethodHandle;

@AllArgsConstructor
public class FieldData {
    private final String name;
    private final String dbName;
    private final boolean canWrite;
    private final boolean canRead;
    private final MethodHandle getter;
    private final MethodHandle setter;
    private final boolean isKey;
}

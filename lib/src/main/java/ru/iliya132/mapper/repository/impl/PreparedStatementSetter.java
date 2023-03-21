package ru.iliya132.mapper.repository.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.iliya132.mapper.helper.Mapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PreparedStatementSetter<TKey, TValue> {
    private final PreparedStatement preparedStatement;
    private int parameterIndex = 0;

    public PreparedStatementSetter(PreparedStatement preparedStatement) {

        this.preparedStatement = preparedStatement;
    }

    private void addBatch() {
        try {
            preparedStatement.addBatch();
            parameterIndex = 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setBool(boolean value) {
        sqlExceptionHandled(() -> preparedStatement.setBoolean(++parameterIndex, value));
    }

    private void setInt(int value) {
        sqlExceptionHandled(() -> preparedStatement.setInt(++parameterIndex, value));
    }

    private void setString(String value) {
        sqlExceptionHandled(() -> preparedStatement.setString(++parameterIndex, value));
    }

    private void setLong(long value) {
        sqlExceptionHandled(() -> preparedStatement.setLong(++parameterIndex, value));
    }

    private void setDouble(double value) {
        sqlExceptionHandled(() -> preparedStatement.setDouble(++parameterIndex, value));
    }

    private void setFloat(float value) {
        sqlExceptionHandled(() -> preparedStatement.setFloat(++parameterIndex, value));
    }

    private void setDate(Date value) {
        sqlExceptionHandled(() -> preparedStatement.setDate(++parameterIndex, value));
    }

    private void setByte(byte value) {
        sqlExceptionHandled(() -> preparedStatement.setByte(++parameterIndex, value));
    }

    private void setBytes(byte[] value) {
        sqlExceptionHandled(() -> preparedStatement.setBytes(++parameterIndex, value));
    }

    private void setTimestamp(Timestamp value) {
        sqlExceptionHandled(() -> preparedStatement.setTimestamp(++parameterIndex, value));
    }

    private void sqlExceptionHandled(Callback callback) {
        try {
            callback.run();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private interface Callback {
        void run() throws SQLException;
    }
}

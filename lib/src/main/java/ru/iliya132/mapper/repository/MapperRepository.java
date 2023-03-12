package ru.iliya132.mapper.repository;

import org.springframework.lang.NonNull;
import ru.iliya132.mapper.helper.Filter;

import java.util.List;
import java.util.Optional;

public interface MapperRepository<TKey, TValue> {
    Optional<TValue> findById(TKey key);
    @NonNull
    List<TValue> findByIds(Iterable<TKey> keys);
    @NonNull
    List<TValue> findByFilter(Filter<TValue> filter);
    Integer update(Iterable<TValue> records);
    Integer delete(Iterable<TKey> keys);
}

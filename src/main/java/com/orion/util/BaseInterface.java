package com.orion.util;

import java.util.List;
import java.util.Optional;

public interface BaseInterface<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    T update(ID id, T entity);
    void deleteById(ID id);
}
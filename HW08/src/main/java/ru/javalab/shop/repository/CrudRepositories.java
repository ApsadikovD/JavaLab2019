package ru.javalab.shop.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepositories<T, ID> {
    Optional<T> findOne(ID id);
    List<T> findAll();
}

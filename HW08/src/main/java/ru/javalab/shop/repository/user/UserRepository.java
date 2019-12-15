package ru.javalab.shop.repository.user;

import ru.javalab.shop.model.User;
import ru.javalab.shop.repository.CrudRepositories;

import java.util.Optional;

public interface UserRepository extends CrudRepositories<User, Integer> {
    Optional<User> findByName(String name);

    void save(User user);
}

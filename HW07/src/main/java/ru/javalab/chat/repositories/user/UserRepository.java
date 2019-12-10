package ru.javalab.chat.repositories.user;

import ru.javalab.chat.model.User;
import ru.javalab.chat.repositories.CrudRepositories;

import java.util.Optional;

public interface UserRepository extends CrudRepositories<User, Integer> {
    Optional<User> findByName(String name);

    void save(User user);
}

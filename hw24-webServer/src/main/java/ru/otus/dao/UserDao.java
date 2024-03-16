package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.model.User;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findRandomUser();

    List<User> getAllUser();

    Optional<User> findByLogin(String login);

    void saveUser(User user);
}

package ru.otus.service;

import ru.otus.model.User;

import java.util.Optional;

public interface UserService {
    Iterable<User> getAllUsers();
    void saveUser(User user);
    void deleteUser(Long id);
    User getRandomUser();
    Optional<User> getUserById(Long id);
}

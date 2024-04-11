package ru.otus.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}

package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import ru.otus.model.User;
import ru.otus.sessionmanager.TransactionManager;

public class UserDaoImpl implements UserDao {
    private final TransactionManager transactionManager;
    private final Class<User> clazz;

    public UserDaoImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.clazz = User.class;
    }

    @Override
    public Optional<User> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> Optional.ofNullable(session.find(clazz, id)));
    }

    @Override
    public Optional<User> findRandomUser() {
        List<User> allUsers = getAllUser();

        if (allUsers.isEmpty()) {
            return Optional.empty(); // Если список пуст, вернуть пустой Optional
        }

        Random random = new Random();
        int randomIndex = random.nextInt(allUsers.size());
        return Optional.of(allUsers.get(randomIndex));
    }

    @Override
    public List<User> getAllUser() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(clazz);
            Root<User> root = query.from(clazz);
            query.select(root);
            return session.createQuery(query).getResultList();
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(clazz);
            Root<User> root = query.from(clazz);
            query.select(root).where(builder.equal(root.get("login"), login));
            User user = session.createQuery(query).uniqueResult();
            return Optional.ofNullable(user);
        });
    }

    @Override
    public void saveUser(User user) {
        transactionManager.doInTransaction(session -> {
            session.persist(user);
            return user;
        });
    }
}

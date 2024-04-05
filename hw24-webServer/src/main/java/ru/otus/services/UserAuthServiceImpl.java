package ru.otus.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import ru.otus.dao.UserDao;
import ru.otus.helpers.PasswordManager;
import ru.otus.model.User;

import java.util.Optional;

public class UserAuthServiceImpl implements UserAuthService {

    private final UserDao userDao;

    public UserAuthServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        PasswordManager passwordManager = new PasswordManager();
        Optional<User> byLogin = userDao.findByLogin(login);
        return passwordManager.checkPassword(password, byLogin.map(User::getPassword).orElse(null));

    }
}

package ru.otus.helpers;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordManager {

    public String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    public boolean checkPassword(String candidatePassword, String hashedPassword) {
        if(candidatePassword==null || hashedPassword==null){
            return false;
        }
        BCrypt.Result result = BCrypt.verifyer().verify(candidatePassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
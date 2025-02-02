package org.example.services;

import org.example.dao.UserDao;
import org.example.entities.Usuario;

public class UserService {
    private UserDao userDao = UserDao.build();

    public Usuario userExists(String email) {
        return userDao.findByEmail(email);
    }

    public Usuario usernameExists(String username) {
        return userDao.findByUsername(username);
    }

    public void checkAndInsertNewUser(Usuario newUser) {
        if (userExists(newUser.getEmail()) == null && usernameExists(newUser.getNombre()) == null) {
            userDao.insertNewUser(newUser);
        }
    }

    public Usuario findUser(String username, String password, String email) {
        return userDao.findUser(username, password, email);
    }
}
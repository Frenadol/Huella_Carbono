package org.example.services;

import org.example.dao.UserDao;
import org.example.entities.Usuario;

import java.util.List;

public class UserService {


    public Usuario userExists(String email) {
        return UserDao.build().findByEmail(email);
    }

    public Usuario usernameExists(String username) {
        return UserDao.build().findByUsername(username);
    }
    public List<Usuario> getAllUsers() {
        return UserDao.build().findAllUsers();
    }
    public List<String> getAllUsernames() {
        return UserDao.build().findAllUsernames();
    }
    public void checkAndInsertNewUser(Usuario newUser) {
        if (userExists(newUser.getEmail()) == null && usernameExists(newUser.getNombre()) == null) {
            UserDao.build().insertNewUser(newUser);
        }
    }

    public boolean verifyUserExists(String username) {
        List<Usuario> allUsers = getAllUsers();
        for (Usuario user : allUsers) {
            if (user.getNombre().equals(username)) {
                return true;
            }
        }
        return false;
    }
    public List<Object[]> getUserImpactByCategory(int userId) {
        return UserDao.build().getUserImpactByCategory(userId);
    }

    public List<Object[]> getAverageImpactByCategory() {
        return UserDao.build().getAverageImpactByCategory();
    }
    public Usuario findUser(String username, String password, String email) {
        return UserDao.build().findUser(username, password, email);
    }

}
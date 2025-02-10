package org.example.services;

import org.example.dao.UserDao;
import org.example.entities.Usuario;
import org.example.utils.AlertsUtils;

import java.util.List;

/**
 * Service class for managing users.
 * This class provides methods to interact with the UserDao for `Usuario` entities.
 */
public class UserService {

    /**
     * Checks if a user exists by email.
     *
     * @param email the email to check.
     * @return the `Usuario` entity if found, null otherwise.
     */
    public Usuario userExists(String email) {
        if (email == null || email.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Email inválido.");
            return null;
        }
        return UserDao.build().findByEmail(email);
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username the username to check.
     * @return the `Usuario` entity if found, null otherwise.
     */
    public Usuario usernameExists(String username) {
        if (username == null || username.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Nombre de usuario inválido.");
            return null;
        }
        return UserDao.build().findByUsername(username);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of `Usuario` entities.
     */
    public List<Usuario> getAllUsers() {
        return UserDao.build().findAllUsers();
    }

    /**
     * Checks if a user exists and inserts a new user if not.
     *
     * @param newUser the new `Usuario` entity to insert.
     */
    public void checkAndInsertNewUser(Usuario newUser) {
        if (newUser == null || newUser.getEmail() == null || newUser.getEmail().isEmpty() || newUser.getNombre() == null || newUser.getNombre().isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Datos del nuevo usuario inválidos.");
            return;
        }
        if (userExists(newUser.getEmail()) == null && usernameExists(newUser.getNombre()) == null) {
            UserDao.build().insertNewUser(newUser);
        }
    }

    /**
     * Checks if a user exists by email.
     *
     * @param email the email to check.
     * @return the `Usuario` entity if found, null otherwise.
     */
    public Usuario userExistsByEmail(String email) {
        if (email == null || email.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Email inválido.");
            return null;
        }
        return UserDao.build().findByEmail(email);
    }

    /**
     * Checks if a user exists by username.
     *
     * @param username the username to check.
     * @return the `Usuario` entity if found, null otherwise.
     */
    public Usuario userExistsByUsername(String username) {
        if (username == null || username.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Nombre de usuario inválido.");
            return null;
        }
        return UserDao.build().findByUsername(username);
    }

    /**
     * Finds a user by username and email.
     *
     * @param username the username to check.
     * @param email the email to check.
     * @return the `Usuario` entity if found, null otherwise.
     */
    public Usuario findUser(String username, String email) {
        if (username == null || username.isEmpty() || email == null || email.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Datos del usuario inválidos.");
            return null;
        }
        return UserDao.build().findUser(username, email);
    }
}
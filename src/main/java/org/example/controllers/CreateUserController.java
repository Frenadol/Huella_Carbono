package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Usuario;
import org.example.services.UserService;
import org.example.utils.AlertsUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class CreateUserController {
    @FXML
    private TextField textUsername;

    @FXML
    private PasswordField textPassword;

    @FXML
    private TextField textEmail;

    UserService userService = new UserService();

    /**
     * Registers a new user.
     * Validates the input fields, checks for existing users, and saves the new user.
     */
    public void createNewUser() throws IOException {
        String username = textUsername.getText();
        String password = textPassword.getText();
        String email = textEmail.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "Por favor, complete todos los campos.");
            return;
        }

        if (!isValidEmail(email)) {
            AlertsUtils.showErrorAlert("Error", "El correo electrónico no tiene un formato válido.");
            return;
        }

        Usuario newUser = new Usuario();
        newUser.setNombre(username);
        newUser.setContraseña(password);
        newUser.setEmail(email);
        newUser.setFechaRegistro(LocalDate.now());

        if (userService.userExists(email) == null) {
            userService.checkAndInsertNewUser(newUser);
            AlertsUtils.showAlert("Éxito", "Usuario registrado exitosamente.");
        } else {
            AlertsUtils.showErrorAlert("Error", "El usuario ya existe.");
        }
    }

    /**
     * Validates the email format.
     * @param email The email to validate.
     * @return true if the email format is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
}
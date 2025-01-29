package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Usuario;
import org.example.services.UserService;

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
            String message = "Por favor, complete todos los campos.";
            showAlert(message);
            return;
        }

        if (!isValidEmail(email)) {
            String message = "El correo electrónico no tiene un formato válido.";
            showAlert(message);
            return;
        }

        Usuario newUser = new Usuario();
        newUser.setNombre(username);
        newUser.setContraseña(password);
        newUser.setEmail(email);
        newUser.setFechaRegistro(LocalDate.now());

        if (!userService.userExists(email)) {
            userService.checkAndInsertNewUser(newUser);
            showAlert("Usuario registrado exitosamente.");

        } else {
            showAlert("El usuario ya existe.");
            App.setRoot("loginUser");
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

    /**
     * Shows an alert with the given message.
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
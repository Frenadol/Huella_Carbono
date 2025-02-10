package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Usuario;
import org.example.services.UserService;
import org.example.utils.AlertsUtils;
import org.example.utils.Security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Controller class for creating a new user.
 */
public class CreateUserController {

    /** TextField for entering the username */
    @FXML
    private TextField textUsername;

    /** PasswordField for entering the password */
    @FXML
    private PasswordField textPassword;

    /** TextField for entering the email */
    @FXML
    private TextField textEmail;

    /** Button for registering the user */
    @FXML
    private Button registerButton;

    /** Service for handling user operations */
    private final UserService userService = new UserService();

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
    }

    /**
     * Creates a new user based on the input fields.
     * Validates the input fields and shows error alerts if necessary.
     * Saves the user if all fields are valid.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void createNewUser() throws IOException {
        String username = textUsername.getText();
        String password = textPassword.getText();
        String email = textEmail.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            AlertsUtils.showEmptyFieldsError();
            return;
        }

        if (!isValidEmail(email)) {
            AlertsUtils.showInvalidEmailError();
            return;
        }

        if (userService.userExistsByEmail(email) != null) {
            AlertsUtils.showEmailExistsError();
            return;
        }

        if (userService.userExistsByUsername(username) != null) {
            AlertsUtils.showUsernameExistsError();
            return;
        }

        try {
            String hashedPassword = Security.hashPassword(password);

            Usuario newUser = new Usuario();
            newUser.setNombre(username);
            newUser.setContraseña(hashedPassword);
            newUser.setEmail(email);
            newUser.setFechaRegistro(LocalDate.now());

            userService.checkAndInsertNewUser(newUser);
            AlertsUtils.showUserCreatedSuccess();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates the email format using a regular expression.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * Navigates back to the first menu.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goBack() throws IOException {
        App.setRoot("FirstMenu");
    }
}
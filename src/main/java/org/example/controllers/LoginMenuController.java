package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Usuario;
import org.example.services.UserService;
import org.example.entities.Session;
import org.example.utils.Security;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Controller class for the login menu.
 */
public class LoginMenuController implements Initializable {

    /** TextField for entering the username */
    @FXML
    private TextField usernameTextField;

    /** PasswordField for entering the password */
    @FXML
    private PasswordField usernamePasswordField;

    /** TextField for entering the email */
    @FXML
    private TextField usernameEmailField;

    /** Service for handling user operations */
    private final UserService userService = new UserService();

    /**
     * Initializes the controller by loading saved fields.
     *
     * @param location the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFields();
    }

    /**
     * Handles the login action.
     * Validates the input fields and shows error alerts if necessary.
     * Logs in the user if all fields are valid.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void login() throws IOException {
        String username = usernameTextField.getText();
        String password = usernamePasswordField.getText();
        String email = usernameEmailField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Todos los campos tienen que estar llenos.");
            return;
        }

        Usuario user = userService.findUser(username,email);
        if (user != null && Security.checkPassword(password, user.getContraseña())) {
            showAlert("Login existoso");
            Session.getInstance().setUser(user);
            App.setRoot("MainMenu");
        } else {
            showAlert("Credenciales incorrectas, vuelve a escribir tus datos.");
        }
        saveFields();
    }

    /**
     * Shows an alert with the given message.
     *
     * @param message the message to display in the alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Saves the input fields to the user's preferences.
     */
    private void saveFields() {
        Preferences.userRoot().node("username").put("username", usernameTextField.getText());
        Preferences.userRoot().node("password").put("password", usernamePasswordField.getText());
        Preferences.userRoot().node("email").put("email", usernameEmailField.getText());
    }

    /**
     * Loads the saved input fields from the user's preferences.
     */
    private void loadFields() {
        usernameTextField.setText(Preferences.userRoot().node("username").get("username", ""));
        usernamePasswordField.setText(Preferences.userRoot().node("password").get("password", ""));
        usernameEmailField.setText(Preferences.userRoot().node("email").get("email", ""));
    }
}
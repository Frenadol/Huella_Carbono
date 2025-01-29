package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.entities.Usuario;
import org.example.services.UserService;
import org.example.utils.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginMenuController implements Initializable {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField usernamePasswordField;

    @FXML
    private TextField usernameEmailField;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFields();
    }

    @FXML
    private void login() throws IOException {
        String username = usernameTextField.getText();
        String password = usernamePasswordField.getText();
        String email = usernameEmailField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Todos los campos son obligatorios.");
            return;
        }

        Usuario user = userService.findUser(username, password, email);
        if (user != null) {
            showAlert("Login exitoso.");
            Session.getInstance().setUser(user);
            App.setRoot("mainMenu");
        } else {
            showAlert("Credenciales incorrectas. Inténtalo de nuevo.");
        }
        saveFields();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveFields() {
        Preferences.userRoot().node("username").put("username", usernameTextField.getText());
        Preferences.userRoot().node("password").put("password", usernamePasswordField.getText());
        Preferences.userRoot().node("email").put("email", usernameEmailField.getText());
    }

    private void loadFields() {
        usernameTextField.setText(Preferences.userRoot().node("username").get("username", ""));
        usernamePasswordField.setText(Preferences.userRoot().node("password").get("password", ""));
        usernameEmailField.setText(Preferences.userRoot().node("email").get("email", ""));
    }
}
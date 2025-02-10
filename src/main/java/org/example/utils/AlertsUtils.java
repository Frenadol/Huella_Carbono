package org.example.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 * Utility class for displaying different types of alerts.
 * This class provides methods to show information, error, and confirmation alerts.
 */
public class AlertsUtils {

    /**
     * Shows an information alert with the given title and content.
     *
     * @param title   the title of the alert.
     * @param content the content of the alert.
     */
    public static void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    /**
     * Shows an error alert with the given title and content.
     *
     * @param title   the title of the alert.
     * @param content the content of the alert.
     */
    public static void showErrorAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    /**
     * Shows an error alert indicating that some fields are empty.
     */
    public static void showEmptyFieldsError() {
        showErrorAlert("Error", "Por favor, complete todos los campos.");
    }

    /**u7u7yy7hjj
     * Shows an error alert indicating that the email format is invalid.
     */
    public static void showInvalidEmailError() {
        showErrorAlert("Error", "El correo electrónico no tiene un formato válido.");
    }

    /**
     * Shows an error alert indicating that the email is already registered.
     */
    public static void showEmailExistsError() {
        showErrorAlert("Error", "El correo electrónico ya está registrado.");
    }

    /**
     * Shows an error alert indicating that the username is already registered.
     */
    public static void showUsernameExistsError() {
        showErrorAlert("Error", "El nombre de usuario ya está registrado.");
    }

    /**
     * Shows an information alert indicating that the user was created successfully.
     */
    public static void showUserCreatedSuccess() {
        showAlert("Éxito", "Usuario registrado exitosamente.");
    }
}
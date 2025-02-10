package org.example.views;

import javafx.fxml.FXML;
import org.example.App;

import java.io.IOException;

/**
 * Controller class for the first menu.
 */
public class FirstMenuController {

    /**
     * Navigates to the login screen.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("Login");
    }

    /**
     * Navigates to the user registration screen.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    private void goToRegister() throws IOException {
        App.setRoot("RegisterUser");
    }
}
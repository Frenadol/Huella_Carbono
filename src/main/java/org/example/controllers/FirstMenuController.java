package org.example.controllers;

import javafx.fxml.FXML;
import org.example.App;

import java.io.IOException;

public class FirstMenuController {

    @FXML
    private void goToLogin() throws IOException {
        App.setRoot("Login");
    }

    @FXML
    private void goToRegister() throws IOException {
        App.setRoot("RegisterUser");
    }
}
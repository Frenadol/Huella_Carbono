package org.example.controllers;

import javafx.fxml.FXML;
import org.example.App;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public void createFingerPrint() throws IOException {
        App.setRoot("CreateFingerPrint");
    }
}

package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.entities.Categoria;
import org.example.services.CategoryService;
import org.example.utils.AlertsUtils;
import org.example.utils.OperationsUtils;

import java.util.List;

public class CategorySelectionDialogController {

    @FXML
    private ComboBox<Categoria> categoryComboBox;
    private static CategoryService categoryService = new CategoryService();
    @FXML
    public void initialize() {
        List<Categoria> categories = categoryService.findAllCategories();
        if (categories != null) {
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        }
    }
}
package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.entities.Categoria;
import org.example.services.CategoryService;
import org.example.utils.AlertsUtils;

import java.util.List;

public class CategorySelectionDialogController {

    @FXML
    private ComboBox<Categoria> categoryComboBox;

    private final CategoryService categoryService = new CategoryService();

    @FXML
    public void initialize() {
        List<Categoria> categories = categoryService.findAllCategories();
        if (categories != null) {
            categoryComboBox.setItems(FXCollections.observableArrayList(categories));
        }
    }

    @FXML
    public void calculateImpactByCategory() {
        Categoria selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            MainMenuController mainMenuController = new MainMenuController();
            mainMenuController.calculateImpactForCategory(selectedCategory);
            Stage stage = (Stage) categoryComboBox.getScene().getWindow();
            stage.close();
        } else {
            AlertsUtils.showErrorAlert("Error", "No category selected.");
        }
    }
}
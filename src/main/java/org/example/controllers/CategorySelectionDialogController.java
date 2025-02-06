package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.example.entities.Categoria;
import org.example.entities.Huella;
import org.example.services.CategoryService;
import org.example.services.FingerprintService;
import org.example.utils.AlertsUtils;
import org.example.utils.OperationsUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @FXML
    public void calculateImpactByCategory() {
        Categoria selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            OperationsUtils.calculateImpactForCategory(selectedCategory);
            Stage stage = (Stage) categoryComboBox.getScene().getWindow();
            stage.close();
        } else {
            AlertsUtils.showErrorAlert("Error", "No category selected.");


        }
    }
}
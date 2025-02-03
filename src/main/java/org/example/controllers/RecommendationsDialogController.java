package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.entities.Recomendacion;
import org.example.services.RecommendationService;
import org.example.utils.AlertsUtils;
import org.example.utils.PDFUtils;
import org.example.utils.Session;

import java.util.List;

public class RecommendationsDialogController {

    @FXML
    private TableView<Recomendacion> recommendationTable;

    @FXML
    private TableColumn<Recomendacion, String> recommendationColumn;

    private RecommendationService recommendationService = new RecommendationService();

    @FXML
    public void initialize() {
        recommendationColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        loadRecommendations();
    }

    private void loadRecommendations() {
        List<Recomendacion> recommendations = recommendationService.getPersonalizedRecommendations(Session.getInstance().getUserLogged().getId());
        ObservableList<Recomendacion> recommendationList = FXCollections.observableArrayList(recommendations);
        recommendationTable.setItems(recommendationList);
    }

    @FXML
    public void generateRecommendationsPdf() {
        try {
            List<Recomendacion> recommendations = recommendationService.getPersonalizedRecommendations(Session.getInstance().getUserLogged().getId());
            PDFUtils.generateRecommendationsPdf(recommendations);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al generar el PDF: " + e.getMessage());
        }
    }
}
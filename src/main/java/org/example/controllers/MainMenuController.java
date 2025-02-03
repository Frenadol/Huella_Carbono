// src/main/java/org/example/controllers/MainMenuController.java
package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.App;
import org.example.dao.FingerPrintDao;
import org.example.dao.RecommendationDao;
import org.example.entities.Huella;
import org.example.entities.Recomendacion;
import org.example.utils.AlertsUtils;
import org.example.utils.PDFUtils;
import org.example.utils.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class MainMenuController {

    @FXML
    private TableView<Huella> fingerprintTable;

    @FXML
    private TableColumn<Huella, String> activityColumn;

    @FXML
    private TableColumn<Huella, String> valueColumn;

    @FXML
    private TableColumn<Huella, String> dateColumn;

    @FXML
    private TableColumn<Huella, String> unitColumn;

    @FXML
    private TableView<Recomendacion> recommendationTable;

    @FXML
    private TableColumn<Recomendacion, String> recommendationColumn;

    @FXML
    public void initialize() {
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        loadFingerprints();
    }

    private void loadFingerprints() {
        FingerPrintDao fingerprintDao = new FingerPrintDao();
        List<Huella> fingerprints = fingerprintDao.viewFingerPrints(Session.getInstance().getUserLogged());
        ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
        fingerprintTable.setItems(fingerprintList);
    }

    private void loadRecommendations() {
        RecommendationDao recommendationDao = new RecommendationDao();
        List<Recomendacion> recommendations = recommendationDao.getRecommendationsByUserId(Session.getInstance().getUserLogged().getId());
        ObservableList<Recomendacion> recommendationList = FXCollections.observableArrayList(recommendations);
        recommendationTable.setItems(recommendationList);
    }

    @FXML
    public void showImpactCalculationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Calculo del Impacto Ambiental");
        alert.setHeaderText("Elija una opción");
        alert.setContentText("Quieres calcularlo con toda tus categorias o elegir una?");

        ButtonType buttonTypeAll = new ButtonType("Todas las huellas");
        ButtonType buttonTypeCategory = new ButtonType("Por categoría");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeAll, buttonTypeCategory, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeAll) {
            calculateImpactForAllFootprints();
        } else if (result.isPresent() && result.get() == buttonTypeCategory) {
            // Implement category-specific calculation if needed
        }
    }

    private void calculateImpactForAllFootprints() {
        try {
            FingerPrintDao fingerprintDao = new FingerPrintDao();
            List<Huella> huellas = fingerprintDao.viewFingerPrints(Session.getInstance().getUserLogged());
            BigDecimal totalImpact = BigDecimal.ZERO;

            for (Huella huella : huellas) {
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                totalImpact = totalImpact.add(impacto);
            }

            totalImpact = totalImpact.setScale(2, RoundingMode.HALF_UP);
            AlertsUtils.showAlert("Impacto total medioambiental", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto medioambiental: " + e.getMessage());
        }
    }

    @FXML
    public void showRecommendationsDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("RecommendationsDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Recomendaciones Personalizadas");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al cargar el diálogo de recomendaciones: " + e.getMessage());
        }
    }
    private void generateRecommendationsPdf() {
        try {
            RecommendationDao recommendationDao = new RecommendationDao();
            List<Recomendacion> recommendations = recommendationDao.getRecommendationsByUserId(Session.getInstance().getUserLogged().getId());
            PDFUtils.generateRecommendationsPdf(recommendations);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al generar el PDF: " + e.getMessage());
        }
    }

    @FXML
    public void createFingerPrint() throws IOException {
        App.setRoot("CreateFingerPrint");
    }

    @FXML
    public void createHabit() throws IOException {
        App.setRoot("CreateHabit");
    }
}
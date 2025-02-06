package org.example.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.example.entities.Recomendacion;
import org.example.services.RecommendationService;
import org.example.utils.AlertsUtils;
import org.example.utils.PDFUtils;
import org.example.utils.Session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class RecommendationsDialogController {

    @FXML
    private TableView<Recomendacion> recommendationTable;

    @FXML
    private TableColumn<Recomendacion, String> recommendationColumn;

    @FXML
    private TableColumn<Recomendacion, Double> impactoEstimadoColumn;

    private RecommendationService recommendationService = new RecommendationService();

    @FXML
    public void initialize() {
        recommendationColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        impactoEstimadoColumn.setCellValueFactory(new PropertyValueFactory<>("impactoEstimado"));
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

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(recommendationTable.getScene().getWindow());

            if (file != null) {
                PDFUtils.generateRecommendationsPdf(recommendations, file.getAbsolutePath());
            }
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al generar el PDF: " + e.getMessage());
        }
    }
}
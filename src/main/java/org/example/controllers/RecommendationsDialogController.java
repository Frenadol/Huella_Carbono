package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.example.entities.Habito;
import org.example.entities.Huella;
import org.example.entities.Recomendacion;
import org.example.entities.Usuario;
import org.example.services.FingerprintService;
import org.example.services.HabitService;
import org.example.services.RecommendationService;
import org.example.utils.AlertsUtils;
import org.example.utils.PDFUtils;
import org.example.entities.Session;

import java.io.File;
import java.util.List;

/**
 * Controller class for the recommendations dialog.
 * This class handles the initialization and interaction of the dialog components,
 * including loading recommendations and generating a PDF report.
 */
public class RecommendationsDialogController {

    /** TableView for displaying recommendations */
    @FXML
    private TableView<Recomendacion> recommendationTable;

    /** TableColumn for displaying the description of a recommendation */
    @FXML
    private TableColumn<Recomendacion, String> recommendationColumn;

    /** TableColumn for displaying the estimated impact of a recommendation */
    @FXML
    private TableColumn<Recomendacion, Double> impactoEstimadoColumn;

    /** Service for handling recommendation operations */
    private RecommendationService recommendationService = new RecommendationService();

    /** Service for handling fingerprint operations */
    private FingerprintService fingerprintService = new FingerprintService();

    /** Service for handling habit operations */
    private HabitService habitService = new HabitService();

    /**
     * Initializes the controller.
     * Sets up the TableView columns and loads the initial data.
     */
    @FXML
    public void initialize() {
        recommendationColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        impactoEstimadoColumn.setCellValueFactory(new PropertyValueFactory<>("impactoEstimado"));
        loadRecommendations();
    }

    /**
     * Loads the recommendations for the logged-in user and displays them in the TableView.
     */
    private void loadRecommendations() {
        List<Recomendacion> recommendations = recommendationService.getPersonalizedRecommendations(Session.getInstance().getUserLogged().getId());
        ObservableList<Recomendacion> recommendationList = FXCollections.observableArrayList(recommendations);
        recommendationTable.setItems(recommendationList);
    }

    /**
     * Generates a PDF report of the recommendations, including the user's fingerprints and habits.
     * This method is triggered by a user action and saves the PDF to a user-specified location.
     */
    @FXML
    public void generateRecommendationsPdf() {
        try {
            Usuario user = Session.getInstance().getUserLogged();
            List<Recomendacion> recommendations = recommendationService.getPersonalizedRecommendations(user.getId());
            List<Huella> fingerprints = fingerprintService.viewFingerPrints(user);
            List<Habito> habits = habitService.getHabitsByUser(user);

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(recommendationTable.getScene().getWindow());

            if (file != null) {
                PDFUtils.generateRecommendationsPdf(user, recommendations, fingerprints, habits, file.getAbsolutePath());
            }
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al generar el PDF: " + e.getMessage());
        }
    }
}
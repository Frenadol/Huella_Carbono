package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.entities.Huella;
import org.example.services.FingerprintService;
import org.example.utils.AlertsUtils;
import org.example.utils.ChartUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Controller class for the select fingerprints dialog.
 * This class handles the initialization and interaction of the dialog components,
 * including loading fingerprints, calculating impact, and selecting all fingerprints.
 */
public class SelectFingerPrintsDialogController {

    /** TableView for displaying fingerprints */
    @FXML
    private TableView<Huella> fingerprintTable;

    /** TableColumn for displaying the activity associated with a fingerprint */
    @FXML
    private TableColumn<Huella, String> activityColumn;

    /** TableColumn for displaying the value of a fingerprint */
    @FXML
    private TableColumn<Huella, String> valueColumn;

    /** TableColumn for displaying the date of a fingerprint */
    @FXML
    private TableColumn<Huella, String> dateColumn;

    /** TableColumn for displaying the unit of a fingerprint */
    @FXML
    private TableColumn<Huella, String> unitColumn;

    /** VBox container for advanced impact calculation */
    @FXML
    private VBox advancedImpactCalculationContainer;

    /** Service for handling fingerprint operations */
    private final FingerprintService fingerprintService = new FingerprintService();

    /**
     * Initializes the controller.
     * Sets up the TableView columns and loads the initial data.
     */
    @FXML
    public void initialize() {
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        loadFingerprints();

        fingerprintTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Loads the fingerprints and displays them in the TableView.
     */
    private void loadFingerprints() {
        List<Huella> fingerprints = fingerprintService.viewFingerPrints();
        if (fingerprints != null) {
            ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
            fingerprintTable.setItems(fingerprintList);
        }
        else {

        }
    }

    /**
     * Calculates the total environmental impact of the selected fingerprints.
     * This method is triggered by a user action and displays the total impact in an alert.
     */
    @FXML
    public void calculateImpact() {
        ObservableList<Huella> selectedFingerprints = fingerprintTable.getSelectionModel().getSelectedItems();
        BigDecimal totalImpact = BigDecimal.ZERO;
        if(selectedFingerprints==null || selectedFingerprints.isEmpty()){
            AlertsUtils.showErrorAlert("No hay huellas seleccionadas", "Por favor seleccione al menos una huella para calcular el impacto medioambiental");
            return;
        }

        for (Huella huella : selectedFingerprints) {
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);
            totalImpact = totalImpact.add(impacto);
        }

        totalImpact = totalImpact.setScale(2, RoundingMode.HALF_UP);
        AlertsUtils.showAlert("Impacto total medioambiental", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");

        ChartUtils.showBarChartForFingerprints(advancedImpactCalculationContainer, selectedFingerprints);
    }

    /**
     * Selects all fingerprints in the TableView.
     * This method is triggered by a user action.
     */
    @FXML
    public void selectAllFingerPrints() {
        fingerprintTable.getSelectionModel().selectAll();
    }
}
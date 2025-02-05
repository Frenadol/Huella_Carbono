package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.entities.Huella;
import org.example.services.FingerprintService;
import org.example.utils.AlertsUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class SelectFingerPrintsDialogController {

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

    private final FingerprintService fingerprintService = new FingerprintService();

    @FXML
    public void initialize() {
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        loadFingerprints();

        fingerprintTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadFingerprints() {
        List<Huella> fingerprints = fingerprintService.viewFingerPrints();
        if (fingerprints != null) {
            ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
            fingerprintTable.setItems(fingerprintList);
        }
    }

    @FXML
    public void calculateImpact() {
        ObservableList<Huella> selectedFingerprints = fingerprintTable.getSelectionModel().getSelectedItems();
        BigDecimal totalImpact = BigDecimal.ZERO;

        for (Huella huella : selectedFingerprints) {
            BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
            BigDecimal impacto = huella.getValor().multiply(factorEmision);
            totalImpact = totalImpact.add(impacto);
        }

        totalImpact = totalImpact.setScale(2, RoundingMode.HALF_UP);
        AlertsUtils.showAlert("Impacto total medioambiental", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
        Stage stage = (Stage) fingerprintTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void selectAllFingerPrints() {
        fingerprintTable.getSelectionModel().selectAll();
    }
}
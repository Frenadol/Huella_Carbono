package org.example.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.Huella;
import org.example.services.FingerprintService;
import org.example.utils.AlertsUtils;
import org.example.utils.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ModifyFingerprintsController {
    @FXML
    private TableView<Huella> fingerprintTable;
    @FXML
    private TableColumn<Huella, String> activityColumn;
    @FXML
    private TableColumn<Huella, BigDecimal> valueColumn;
    @FXML
    private TableColumn<Huella, String> unitColumn;
    @FXML
    private TableColumn<Huella, LocalDate> dateColumn;

    private final FingerprintService fingerprintService = new FingerprintService();

    @FXML
    public void initialize() {
        activityColumn.setCellValueFactory(cellData -> {
            Huella huella = cellData.getValue();
            String activityName = huella.getIdActividad() != null ? huella.getIdActividad().getNombre() : null;
            return new SimpleStringProperty(activityName);
        });
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        loadFingerprints();
    }

    private void loadFingerprints() {
        List<Huella> fingerprints = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
        fingerprintTable.setItems(fingerprintList);
    }

    @FXML
    public void deleteFingerprint() {
        Huella fingerprint = fingerprintTable.getSelectionModel().getSelectedItem();
        if (fingerprint != null) {
            fingerprintService.deleteFingerPrint(fingerprint);
            loadFingerprints();
        } else {
            AlertsUtils.showErrorAlert("Error", "No fingerprint selected.");
        }
    }

    @FXML
    public void modifyFingerprint() {
        Huella fingerprint = fingerprintTable.getSelectionModel().getSelectedItem();
        if (fingerprint != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("ModifyFingerprintDialog.fxml"));
                Parent parent = loader.load();
                ModifyFingerprintsDialogController controller = loader.getController();
                controller.setSelectedFingerPrint(fingerprint);
                Stage stage = new Stage();
                stage.setTitle("Modificacion de Huella");
                stage.setScene(new Scene(parent));
                stage.showAndWait();
                loadFingerprints();
            } catch (IOException e) {
                AlertsUtils.showErrorAlert("Error", "Fallo al cargar la ventana: " + e.getMessage());
            }
        } else {
            AlertsUtils.showErrorAlert("Error", "No has seleccionado ninguna huella.");
        }
    }

    @FXML
    public void goToMainMenu() throws IOException {
        App.setRoot("MainMenu");
    }
}
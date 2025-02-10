package org.example.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.services.FingerprintService;
import org.example.services.ActivityService;
import org.example.utils.AlertsUtils;
import org.example.views.MainMenuController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class for the modify fingerprints dialog.
 * This class handles the initialization and interaction of the dialog components,
 * including setting the selected fingerprint, saving changes, and deleting fingerprints.
 */
public class ModifyFingerprintsDialogController {
    /** ComboBox for selecting an activity */
    @FXML
    private ComboBox<Actividad> activityComboBox;

    /** TextField for entering the value of the fingerprint */
    @FXML
    private TextField valueField;

    /** DatePicker for selecting the date of the fingerprint */
    @FXML
    private DatePicker datePicker;

    /** The selected fingerprint to be modified */
    private Huella selectedFingerPrint;

    /** Service for handling fingerprint operations */
    private final FingerprintService fingerprintService = new FingerprintService();

    /** Service for handling activity operations */
    private final ActivityService activityService = new ActivityService();

    /** Reference to the MainMenuController */
    private MainMenuController mainMenuController;

    /**
     * Sets the selected fingerprint to be modified.
     * Populates the dialog fields with the details of the selected fingerprint.
     *
     * @param selectedFingerPrint the selected fingerprint
     */
    public void setSelectedFingerPrint(Huella selectedFingerPrint) {
        this.selectedFingerPrint = selectedFingerPrint;
        activityComboBox.setValue(selectedFingerPrint.getIdActividad());
        valueField.setText(selectedFingerPrint.getValor().toString());
        datePicker.setValue(selectedFingerPrint.getFecha() != null ? selectedFingerPrint.getFecha().toLocalDate() : LocalDate.now());

    }

    /**
     * Sets the reference to the MainMenuController.
     *
     * @param mainMenuController the MainMenuController
     */
    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    /**
     * Initializes the controller.
     * Loads the list of activities and sets them in the ComboBox.
     */
    @FXML
    public void initialize() {
        List<Actividad> activities = activityService.findAllWithCategories();
        activityComboBox.setItems(FXCollections.observableArrayList(activities));
    }

    /**
     * Saves the changes made to the selected fingerprint.
     * Updates the fingerprint details and closes the dialog.
     */
    @FXML
    public void saveChanges() {
        Actividad selectedActivity = activityComboBox.getValue();
        String valueText = valueField.getText();
        LocalDate newDate = datePicker.getValue();

        if (selectedActivity == null || valueText == null || valueText.isEmpty() || newDate == null) {
            AlertsUtils.showErrorAlert("Error", "Todos los campos deben estar completos.");
            return;
        }

        BigDecimal newValue = new BigDecimal(valueText);
        String newUnit = selectedActivity.getIdCategoria().getUnidad();
        LocalDateTime newDateTime = newDate.atStartOfDay();
        fingerprintService.updateFingerPrintDetails(selectedFingerPrint, selectedActivity, newValue, newUnit, newDateTime);
        closeDialog();
        if (mainMenuController != null) {
            mainMenuController.refreshTable();
        }
    }

    /**
     * Deletes the selected fingerprint.
     * Removes the fingerprint from the database and closes the dialog.
     */
    @FXML
    public void deleteFingerPrint() {
        fingerprintService.deleteFingerPrint(selectedFingerPrint);
        Platform.runLater(() -> mainMenuController.loadFingerprints());
        closeDialog();
    }
    /**
     * Closes the dialog.
     * This method is triggered by a user action and closes the current stage.
     */
    @FXML
    public void closeDialog() {
        Stage stage = (Stage) activityComboBox.getScene().getWindow();
        stage.close();
    }
}
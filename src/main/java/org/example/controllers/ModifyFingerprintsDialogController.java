package org.example.controllers;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ModifyFingerprintsDialogController {
    @FXML
    private ComboBox<Actividad> activityComboBox;
    @FXML
    private TextField valueField;
    @FXML
    private DatePicker datePicker;

    private Huella selectedFingerPrint;
    private final FingerprintService fingerprintService = new FingerprintService();
    private final ActivityService activityService = new ActivityService();

    public void setSelectedFingerPrint(Huella selectedFingerPrint) {
        this.selectedFingerPrint = selectedFingerPrint;
        activityComboBox.setValue(selectedFingerPrint.getIdActividad());
        valueField.setText(selectedFingerPrint.getValor().toString());
        datePicker.setValue(selectedFingerPrint.getFecha() != null ? selectedFingerPrint.getFecha().toLocalDate() : LocalDate.now());
    }

    @FXML
    public void initialize() {
        List<Actividad> activities = activityService.findAllWithCategories();
        activityComboBox.setItems(FXCollections.observableArrayList(activities));
    }

    @FXML
    public void saveChanges() {
        Actividad selectedActivity = activityComboBox.getValue();
        BigDecimal newValue = new BigDecimal(valueField.getText());
        LocalDate newDate = datePicker.getValue();
        String newUnit = selectedActivity.getIdCategoria().getUnidad();

        // Convert LocalDate to LocalDateTime
        LocalDateTime newDateTime = newDate.atStartOfDay();

        fingerprintService.updateFingerPrintDetails(selectedFingerPrint, selectedActivity, newValue, newUnit, newDateTime);
        closeDialog();
    }

    @FXML
    public void closeDialog() {
        Stage stage = (Stage) activityComboBox.getScene().getWindow();
        stage.close();
    }
}
package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import org.example.App;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.services.ActivityService;
import org.example.services.FingerprintService;
import org.example.utils.AlertsUtils;
import org.example.utils.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreateFingerprintController {

    @FXML
    private ComboBox<Actividad> activityComboBox;

    @FXML
    private TextField valueField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField unitField;

    private final FingerprintService fingerprintService = new FingerprintService();
    private final ActivityService activityService = new ActivityService();

    @FXML
    public void initialize() {
        loadActivities();
        activityComboBox.setOnAction(event -> updateUnitField());
    }

    private void loadActivities() {
        List<Actividad> activities = activityService.findAllWithCategories();
        if (activities != null) {
            activityComboBox.getItems().addAll(activities);
        }
    }

    private void updateUnitField() {
        Actividad selectedActivity = activityComboBox.getValue();
        if (selectedActivity != null && selectedActivity.getIdCategoria() != null) {
            unitField.setText(selectedActivity.getIdCategoria().getUnidad());
        }
    }

    @FXML
    private void registerFingerprint() {
        try {
            Actividad selectedActivity = activityComboBox.getValue();
            String value = valueField.getText();
            LocalDate date = datePicker.getValue();
            String unit = unitField.getText();

            if (selectedActivity == null || value.isEmpty() || date == null || unit.isEmpty()) {
                AlertsUtils.showErrorAlert("Error", "Todos los campos son obligatorios.");
                return;
            }

            Huella huella = new Huella();
            huella.setIdUsuario(Session.getInstance().getUserLogged());
            huella.setIdActividad(selectedActivity);
            huella.setValor(BigDecimal.valueOf(Double.parseDouble(value)));
            huella.setFecha(LocalDateTime.now());
            huella.setUnidad(unit);

            fingerprintService.saveFingerprint(huella);
        } catch (NumberFormatException e) {
            AlertsUtils.showErrorAlert("Error", "El valor debe ser un número válido.");
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Ocurrió un error al registrar la huella: " + e.getMessage());
        }
    }

    @FXML
    public void goToMainMenu() throws IOException {
        App.setRoot("MainMenu");
    }
}
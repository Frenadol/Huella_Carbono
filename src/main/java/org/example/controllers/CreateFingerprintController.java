package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import org.example.dao.ActivityDao;
import org.example.dao.FingerPrintDao;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.utils.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @FXML
    public void initialize() {
        loadActivities();
        activityComboBox.setOnAction(event -> updateUnitField());
    }

    private void loadActivities() {
        ActivityDao activityDao = new ActivityDao();
        List<Actividad> activities = activityDao.findAll();
        activityComboBox.getItems().addAll(activities);
    }

    private void updateUnitField() {
        Actividad selectedActivity = activityComboBox.getValue();
        if (selectedActivity != null) {
            switch (selectedActivity.getNombre().toLowerCase()) {
                case "transporte":
                    unitField.setText("km");
                    break;
                case "electricidad":
                    unitField.setText("kWh");
                    break;
                default:
                    unitField.setText("unidad");
                    break;
            }
        }
    }

    @FXML
    private void registerFingerprint() {
        Actividad selectedActivity = activityComboBox.getValue();
        String value = valueField.getText();
        LocalDate date = datePicker.getValue();
        String unit = unitField.getText();

        if (selectedActivity == null || value.isEmpty() || date == null || unit.isEmpty()) {
            showAlert("Error", "Todos los campos son obligatorios.");
            return;
        }

        Huella huella = new Huella();
        huella.setIdUsuario(Session.getInstance().getUserLogged());
        huella.setIdActividad(selectedActivity);
        huella.setValor(BigDecimal.valueOf(Double.parseDouble(value)));
        huella.setFecha(LocalDate.now());
        huella.setUnidad(unit);

        FingerPrintDao fingerprintDao = new FingerPrintDao();
        fingerprintDao.save(huella);

        showAlert("Éxito", "Huella registrada correctamente.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
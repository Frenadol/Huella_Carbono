package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import org.example.App;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.services.ActivityService;
import org.example.services.FingerprintService;
import org.example.utils.AlertsUtils;
import org.example.entities.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class CreateFingerprintController {

    /** ComboBox for selecting an activity */
    @FXML
    private ComboBox<Actividad> activityComboBox;

    /** TextField for entering the value */
    @FXML
    private TextField valueField;

    /** DatePicker for selecting the date */
    @FXML
    private DatePicker datePicker;

    /** TextField for displaying the unit */
    @FXML
    private TextField unitField;

    /** Service for handling fingerprint operations */
    private final FingerprintService fingerprintService = new FingerprintService();

    /** Service for handling activity operations */
    private final ActivityService activityService = new ActivityService();

    /**
     * Method called automatically after the FXML file has been loaded.
     * Initializes the controller by loading activities and setting up event handlers.
     */
    @FXML
    public void initialize() {
        loadActivities();
        activityComboBox.setOnAction(event -> updateUnitField());
        restrictFutureDates();
    }

    /**
     * Loads all activities and adds them to the ComboBox.
     */
    private void loadActivities() {
        List<Actividad> activities = activityService.findAllWithCategories();
        if (activities != null) {
            activityComboBox.getItems().addAll(activities);
        }
    }

    /**
     * Updates the unit field based on the selected activity.
     */
    private void updateUnitField() {
        Actividad selectedActivity = activityComboBox.getValue();
        if (selectedActivity != null && selectedActivity.getIdCategoria() != null) {
            unitField.setText(selectedActivity.getIdCategoria().getUnidad());
        }
    }

    /**
     * Restricts the DatePicker to not allow future dates.
     */
    private void restrictFutureDates() {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isAfter(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    /**
     * Registers a new fingerprint.
     * Validates the input fields and shows error alerts if necessary.
     * Saves the fingerprint if all fields are valid.
     */
    @FXML
    private void registerFingerprint() {
        try {
            Actividad selectedActivity = activityComboBox.getValue();
            String value = valueField.getText();
            LocalDate date = datePicker.getValue();
            String unit = unitField.getText();

            if (selectedActivity == null || value == null || value.isEmpty() || date == null || unit == null || unit.isEmpty()) {
                AlertsUtils.showErrorAlert("Error", "Todos los campos son obligatorios.");
                return;
            }

            Huella huella = new Huella();
            huella.setIdUsuario(Session.getInstance().getUserLogged());
            huella.setIdActividad(selectedActivity);
            huella.setValor(BigDecimal.valueOf(Double.parseDouble(value)));
            huella.setFecha(LocalDateTime.of(date, LocalTime.now()));
            huella.setUnidad(unit);

            fingerprintService.saveFingerprint(huella);
        } catch (NumberFormatException e) {
            AlertsUtils.showErrorAlert("Error", "El valor debe ser un número válido.");
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Ocurrió un error al registrar la huella: " + e.getMessage());
        }
    }

    /**
     * Navigates to the main menu.
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToMainMenu() throws IOException {
        App.setRoot("MainMenu");
    }
}
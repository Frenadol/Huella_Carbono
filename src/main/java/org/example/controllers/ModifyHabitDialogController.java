package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.Habito;
import org.example.services.HabitService;
import org.example.utils.AlertsUtils;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller class for the modify habit dialog.
 * This class handles the initialization and interaction of the dialog components,
 * including setting the selected habit, saving changes, and navigating back to the modify habits screen.
 */
public class ModifyHabitDialogController {

    /** ComboBox for selecting the frequency of the habit */
    @FXML
    private ComboBox<String> frequencyField;

    /** ComboBox for selecting the type of the habit */
    @FXML
    private ComboBox<String> typeField;

    /** DatePicker for selecting the start date of the habit */
    @FXML
    private DatePicker startDatePicker;

    /** The selected habit to be modified */
    private Habito habit;

    /** Service for handling habit operations */
    private final HabitService habitService = new HabitService();

    /**
     * Initializes the controller.
     * Loads the frequency and type options, and configures the DatePicker.
     */
    @FXML
    public void initialize() {
        loadFrequencyOptions();
        loadTypeOptions();
        configureDatePicker();
    }

    /**
     * Loads the frequency options into the ComboBox.
     */
    private void loadFrequencyOptions() {
        for (int i = 1; i <= 10; i++) {
            frequencyField.getItems().add(String.valueOf(i));
        }
    }

    /**
     * Loads the type options into the ComboBox.
     */
    private void loadTypeOptions() {
        typeField.getItems().addAll("Diariamente", "Semanalmente", "Mensualmente", "Anualmente");
    }

    /**
     * Configures the DatePicker to disable future dates.
     */
    private void configureDatePicker() {
        if (startDatePicker == null) {
            AlertsUtils.showErrorAlert("Error", "No has elegido ninguna fecha");
        }
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
        startDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets the selected habit to be modified.
     * Populates the dialog fields with the details of the selected habit.
     *
     * @param habit the selected habit
     */
    public void setHabit(Habito habit) {
        this.habit = habit;
        frequencyField.setValue(habit.getFrecuencia());
        typeField.setValue(habit.getTipo());
        startDatePicker.setValue(habit.getUltimaFecha().toLocalDate());
    }

    /**
     * Saves the changes made to the selected habit.
     * Updates the habit details and closes the dialog.
     */
    @FXML
    public void saveHabit() {
        try {
            habit.setFrecuencia(frequencyField.getValue());
            habit.setTipo(typeField.getValue());
            habit.setUltimaFecha(startDatePicker.getValue().atStartOfDay());
            habitService.updateHabit(habit);
            AlertsUtils.showAlert("Success", "Habit updated successfully.");
            Stage stage = (Stage) frequencyField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Failed to update habit: " + e.getMessage());
        }
    }

    /**
     * Navigates back to the modify habits screen.
     * This method is triggered by a user action and closes the current stage.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void backToModifyHabits() throws IOException {
        Stage stage = (Stage) frequencyField.getScene().getWindow();
        stage.close();
    }
}
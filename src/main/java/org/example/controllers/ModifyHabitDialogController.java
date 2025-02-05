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

public class ModifyHabitDialogController {

    @FXML
    private ComboBox<String> frequencyField;

    @FXML
    private ComboBox<String> typeField;

    @FXML
    private DatePicker startDatePicker;

    private Habito habit;
    private final HabitService habitService = new HabitService();

    @FXML
    public void initialize() {
        loadFrequencyOptions();
        loadTypeOptions();
        configureDatePicker();
    }

    private void loadFrequencyOptions() {
        for (int i = 1; i <= 10; i++) {
            frequencyField.getItems().add(String.valueOf(i));
        }
    }

    private void loadTypeOptions() {
        typeField.getItems().addAll("Diariamente", "Semanalmente", "Mensualmente", "Anualmente");
    }

    private void configureDatePicker() {
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isAfter(LocalDate.now()));
            }
        });
        startDatePicker.setValue(LocalDate.now());
    }

    public void setHabit(Habito habit) {
        this.habit = habit;
        frequencyField.setValue(habit.getFrecuencia());
        typeField.setValue(habit.getTipo());
        startDatePicker.setValue(habit.getUltimaFecha().toLocalDate());
    }

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
    @FXML
    public void backToModifyHabits() throws IOException {
        Stage stage = (Stage) frequencyField.getScene().getWindow();
        stage.close();
    }
}
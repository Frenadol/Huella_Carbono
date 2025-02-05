package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import org.example.App;
import org.example.entities.Actividad;
import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.example.entities.Usuario;
import org.example.services.ActivityService;
import org.example.services.HabitService;
import org.example.utils.AlertsUtils;
import org.example.utils.Session;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreateHabitController {

    @FXML
    private ComboBox<Actividad> activityComboBox;

    @FXML
    private ComboBox<Integer> frequencyComboBox;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private DatePicker startDatePicker;

    private final HabitService habitService;
    private final ActivityService activityService;

    public CreateHabitController() {
        this.habitService = new HabitService();
        this.activityService = new ActivityService();
    }

    @FXML
    public void initialize() {
        loadActivities();
        loadFrequencyOptions();
        loadTypeOptions();
    }

    private void loadActivities() {
        List<Actividad> activities = activityService.findAllWithCategories();
        if (activities != null) {
            activityComboBox.getItems().addAll(activities);
        }
    }

    private void loadFrequencyOptions() {
        for (int i = 1; i <= 10; i++) {
            frequencyComboBox.getItems().add(i);
        }
    }

    private void loadTypeOptions() {
        typeComboBox.getItems().addAll("Diariamente", "Semanalmente", "Mensualmente", "Anualmente");
    }

    @FXML
    private void registerHabit() {
        Actividad selectedActivity = activityComboBox.getValue();
        Integer frequency = frequencyComboBox.getValue();
        String type = typeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();

        if (selectedActivity == null || frequency == null || type == null || startDate == null) {
            showAlert("Error", "Todos los campos son obligatorios.");
            return;
        }

        HabitoId habitoId = new HabitoId();
        habitoId.setIdUsuario(Session.getInstance().getUserId());
        habitoId.setIdActividad(selectedActivity.getId());

        Habito newHabito = new Habito();
        newHabito.setId(habitoId);

        Usuario usuario = new Usuario();
        usuario.setId(Session.getInstance().getUserId());
        newHabito.setIdUsuario(usuario);

        Actividad actividad = new Actividad();
        actividad.setId(selectedActivity.getId());
        newHabito.setIdActividad(actividad);

        newHabito.setFrecuencia(String.valueOf(frequency));
        newHabito.setTipo(type);
        newHabito.setUltimaFecha(LocalDateTime.now());

        if (habitService.insertHabit(newHabito)) {
            showAlert("Éxito", "Hábito registrado correctamente.");
        }
    }

    private void showAlert(String title, String message) {
        AlertsUtils.showAlert(title, message);
    }

    @FXML
    public void goToMainMenu() throws IOException {
        App.setRoot("MainMenu");
    }
}
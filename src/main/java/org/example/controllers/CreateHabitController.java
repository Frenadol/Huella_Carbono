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
import org.example.entities.Session;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CreateHabitController {

    /** ComboBox for selecting an activity */
    @FXML
    private ComboBox<Actividad> activityComboBox;

    /** ComboBox for selecting the frequency of the habit */
    @FXML
    private ComboBox<Integer> frequencyComboBox;

    /** ComboBox for selecting the type of the habit */
    @FXML
    private ComboBox<String> typeComboBox;

    /** DatePicker for selecting the start date of the habit */
    @FXML
    private DatePicker startDatePicker;

    /** Service for handling habit operations */
    private final HabitService habitService;

    /** Service for handling activity operations */
    private final ActivityService activityService;

    /**
     * Constructor initializes the services for habit and activity operations.
     */
    public CreateHabitController() {
        this.habitService = new HabitService();
        this.activityService = new ActivityService();
    }

    /**
     * Method called automatically after the FXML file has been loaded.
     * Initializes the controller by loading activities, frequency options, and type options.
     */
    @FXML
    public void initialize() {
        loadActivities();
        loadFrequencyOptions();
        loadTypeOptions();
    }

    /**
     * Loads all activities and adds them to the activity ComboBox.
     */
    private void loadActivities() {
        List<Actividad> activities = activityService.findAllWithCategories();
        if (activities != null) {
            activityComboBox.getItems().addAll(activities);
        }
    }

    /**
     * Loads frequency options (1 to 10) and adds them to the frequency ComboBox.
     */
    private void loadFrequencyOptions() {
        for (int i = 1; i <= 10; i++) {
            frequencyComboBox.getItems().add(i);
        }
    }

    /**
     * Loads type options and adds them to the type ComboBox.
     * The options are "Diariamente", "Semanalmente", "Mensualmente", and "Anualmente".
     */
    private void loadTypeOptions() {
        typeComboBox.getItems().addAll("Diariamente", "Semanalmente", "Mensualmente", "Anualmente");
    }

    /**
     * Registers a new habit based on the selected activity, frequency, type, and start date.
     * Validates the input fields and shows error alerts if necessary.
     * Saves the habit if all fields are valid.
     */
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

    /**
     * Shows an alert with the given title and message.
     * @param title the title of the alert
     * @param message the message of the alert
     */
    private void showAlert(String title, String message) {
        AlertsUtils.showAlert(title, message);
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
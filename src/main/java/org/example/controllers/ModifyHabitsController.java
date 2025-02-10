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
import org.example.entities.Habito;
import org.example.services.HabitService;
import org.example.utils.AlertsUtils;
import org.example.entities.Session;

import java.io.IOException;
import java.util.List;

/**
 * Controller class for modifying habits.
 * This class handles the initialization and interaction of the habit modification components,
 * including loading, deleting, and modifying habits.
 */
public class ModifyHabitsController {
    /** TableView for displaying habits */
    @FXML
    private TableView<Habito> habitTable;

    /** TableColumn for displaying the name of the activity associated with a habit */
    @FXML
    private TableColumn<Habito, String> habitNameColumn;

    /** TableColumn for displaying the frequency of a habit */
    @FXML
    private TableColumn<Habito, Integer> habitFrequencyColumn;

    /** TableColumn for displaying the type of a habit */
    @FXML
    private TableColumn<Habito, String> habitTypeColumn;

    /** Service for handling habit operations */
    private final HabitService habitService = new HabitService();

    /**
     * Initializes the controller.
     * Sets up the TableView columns and loads the initial data.
     */
    @FXML
    public void initialize() {
        habitNameColumn.setCellValueFactory(cellData -> {
            Habito habito = cellData.getValue();
            String activityName = habito.getIdActividad() != null ? habito.getIdActividad().getNombre() : null;
            return new SimpleStringProperty(activityName);
        });
        habitFrequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        habitTypeColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        loadHabits();
    }

    /**
     * Loads the habits for the logged-in user and displays them in the TableView.
     */
    private void loadHabits() {
        List<Habito> habits = habitService.getHabitsByUser(Session.getInstance().getUserLogged());
        ObservableList<Habito> habitList = FXCollections.observableArrayList(habits);
        habitTable.setItems(habitList);
    }

    /**
     * Deletes the selected habit.
     * This method is triggered by a user action and removes the selected habit from the database.
     */
    @FXML
    public void deleteHabit() {
        Habito habit = habitTable.getSelectionModel().getSelectedItem();
        if (habit != null) {
            habitService.deleteHabit(habit);
            loadHabits(); // Reload the habits to refresh the TableView
        } else {
            AlertsUtils.showErrorAlert("Error", "Ningún hábito seleccionado.");
        }
    }
    /**
     * Modifies the selected habit.
     * This method is triggered by a user action, loads the FXML for the modify habit dialog,
     * and displays it in a new stage. After modification, it reloads the habits.
     */
    @FXML
    public void modifyHabit() {
        Habito habit = habitTable.getSelectionModel().getSelectedItem();
        if (habit != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("ModifyHabitDialog.fxml"));
                Parent parent = loader.load();
                ModifyHabitDialogController controller = loader.getController();
                controller.setHabit(habit);
                Stage stage = new Stage();
                stage.setTitle("Modify Habit");
                stage.setScene(new Scene(parent));
                stage.showAndWait();
                loadHabits();
            } catch (IOException e) {
                AlertsUtils.showErrorAlert("Error", "Failed to load modify habit dialog: " + e.getMessage());
            }
        } else {
            AlertsUtils.showErrorAlert("Error", "Ningún hábito seleccionado.");
        }
    }

    /**
     * Navigates to the main menu.
     * This method is triggered by a user action and changes the root scene to the main menu.
     *
     * @throws IOException if an I/O error occurs
     */
    @FXML
    public void goToMainMenu() throws IOException {
        App.setRoot("MainMenu");
    }

}
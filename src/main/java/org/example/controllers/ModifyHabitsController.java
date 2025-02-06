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
import org.example.utils.Session;

import java.io.IOException;
import java.util.List;

public class ModifyHabitsController {
    @FXML
    private TableView<Habito> habitTable;
    @FXML
    private TableColumn<Habito, String> habitNameColumn;
    @FXML
    private TableColumn<Habito, Integer> habitFrequencyColumn;
    @FXML
    private TableColumn<Habito, String> habitTypeColumn;

    private final HabitService habitService = new HabitService();

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

    private void loadHabits() {
        List<Habito> habits = habitService.getHabitsByUser(Session.getInstance().getUserLogged());
        ObservableList<Habito> habitList = FXCollections.observableArrayList(habits);
        habitTable.setItems(habitList);
    }

    @FXML
    public void deleteHabit() {
        Habito habit = habitTable.getSelectionModel().getSelectedItem();
        if (habit != null) {
            habitService.deleteHabit(habit);
            loadHabits();
        } else {
            AlertsUtils.showErrorAlert("Error", "Ningún hábito seleccionado.");
        }
    }

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
            AlertsUtils.showErrorAlert("Error", "Ningun habito seleccionado.");
        }
    }
    @FXML
    public void goToMainMenu() throws IOException {
        App.setRoot("MainMenu");
    }
}
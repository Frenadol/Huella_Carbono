package org.example.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.*;
import org.example.services.FingerprintService;
import org.example.services.HabitService;
import org.example.services.UserService;
import org.example.utils.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for the main menu.
 * This class handles the initialization and interaction of the main menu components,
 * including the display and calculation of environmental impacts based on user habits and footprints.
 */
public class MainMenuController {

    @FXML
    private TableView<Huella> fingerprintTable;

    @FXML
    private ImageView helpCalculationHabits;


    @FXML
    private ImageView helpHabits;

    @FXML
    private ImageView fingerprintsHabits;

    @FXML
    private TableColumn<Usuario, BigDecimal> averageImpactColumn;

    @FXML
    private ChoiceBox<String> calculationChoiceBox;

    @FXML
    private TableColumn<Huella, String> activityColumn;

    @FXML
    private TableColumn<Huella, String> valueColumn;

    @FXML
    private TableColumn<Huella, String> dateColumn;

    @FXML
    private TableColumn<Huella, String> unitColumn;

    @FXML
    private TableView<Habito> habitTable;

    @FXML
    private VBox advancedImpactCalculationContainer;

    @FXML
    private TableColumn<Habito, String> habitActivityNameColumn;

    @FXML
    private TableColumn<Habito, String> habitFrecuenciaColumn;

    @FXML
    private TableColumn<Habito, String> habitTipoColumn;

    @FXML
    private TableColumn<Habito, String> habitUltimaFechaColumn;

    @FXML
    private VBox chartContainer;

    @FXML
    private TableView<Usuario> comparisonTable;

    @FXML
    private TableColumn<Usuario, String> comparisonUsernameColumn;

    @FXML
    private TableColumn<Usuario, BigDecimal> totalImpactColumn;

    private final FingerprintService fingerprintService = new FingerprintService();
    private final HabitService habitService = new HabitService();
    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        loadHabits();
        helpCalculationHabits.setOnMouseClicked(event -> HelpsUtils.showEnvironmentalImpactHelp());
        helpHabits.setOnMouseClicked(event -> HelpsUtils.showHabitsHelp());
        fingerprintsHabits.setOnMouseClicked(event -> HelpsUtils.showFootprintsHelp());
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        calculationChoiceBox.setItems(FXCollections.observableArrayList("Mensual", "Semanal"));
        calculationChoiceBox.setOnAction(event -> handleCalculationChoice());
        habitActivityNameColumn.setCellValueFactory(cellData -> {
            Habito habito = cellData.getValue();
            String activityName = habito.getIdActividad() != null ? habito.getIdActividad().getNombre() : null;
            return new SimpleStringProperty(activityName);
        });
        habitFrecuenciaColumn.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        habitTipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        habitUltimaFechaColumn.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));
        comparisonUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        totalImpactColumn.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            BigDecimal impactoTotal = OperationsUtils.calculateImpactoTotal(usuario);
            return new SimpleObjectProperty<>(impactoTotal);
        });
        averageImpactColumn.setCellValueFactory(cellData -> {
            Usuario usuario = cellData.getValue();
            BigDecimal averageImpact = OperationsUtils.calculateAverageImpactForUser(usuario);
            return new SimpleObjectProperty<>(averageImpact);
        });
        loadFingerprints();
        loadHabits();
        loadUsernames();
    }

    @FXML
    private void handleCalculationChoice() {
        chartContainer.getChildren().clear();
        String choice = calculationChoiceBox.getValue();
        if (choice != null) {
            switch (choice) {
                case "Mensual":
                    calculateMonthlyImpact();
                    break;
                case "Semanal":
                    calculateWeeklyImpact();
                    break;
            }
        }
    }
    @FXML
    public void loadFingerprints() {
        List<Huella> fingerprints = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
        fingerprintTable.setItems(fingerprintList);
    }

    private void loadHabits() {
        List<Habito> habits = habitService.getHabitsByUser(Session.getInstance().getUserLogged());
        if (habits != null) {
            ObservableList<Habito> habitList = FXCollections.observableArrayList(habits);
            habitTable.setItems(habitList);
        }
    }

    private void loadUsernames() {
        List<Usuario> users = userService.getAllUsers();
        if (users != null) {
            ObservableList<Usuario> userList = FXCollections.observableArrayList(users);
            comparisonTable.setItems(userList);
        }
    }

    private void showOperationSelectionDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Seleccionar Operación");
        alert.setHeaderText("Elija una operación");
        alert.setContentText("Seleccione la operación que desea realizar:");

        ButtonType buttonTypeAll = new ButtonType("Calcular impacto de todas las huellas");
        ButtonType buttonTypeCategory = new ButtonType("Calcular por categoría");
        ButtonType buttonTypeSelect = new ButtonType("Elegir huellas específicas");
        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeAll, buttonTypeCategory, buttonTypeSelect, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonTypeAll) {
                calculateImpactForAllFootprints();
            } else if (result.get() == buttonTypeCategory) {
                calculateImpactForAllCategories();
            } else if (result.get() == buttonTypeSelect) {
                showSelectFingerprintsDialog();
            }
        }
    }

    @FXML
    public void showImpactCalculationDialog() {
        showOperationSelectionDialog();
    }

    private void showSelectFingerprintsDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("SelectFingerprintsDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Seleccionar Huellas");
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateImpactForAllFootprints() {
        List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        if (huellas == null || huellas.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No hay huellas para calcular el impacto.");
            return;
        }
        try {
            BigDecimal totalImpact = OperationsUtils.calculateImpactForAllFootprints(huellas);
            showAlert("Impacto total medioambiental", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
            advancedImpactCalculationContainer.getChildren().clear();
            ChartUtils.showBarChartForFingerprints(advancedImpactCalculationContainer, huellas);
        } catch (Exception e) {
        }
    }

    private void calculateImpactForAllCategories() {
        List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        if (huellas == null || huellas.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No hay huellas para calcular el impacto.");
            return;
        }
        try {
            Map<String, BigDecimal> categoryImpactMap = OperationsUtils.calculateImpactForAllCategories(huellas);
            advancedImpactCalculationContainer.getChildren().clear();
            ChartUtils.showBarChartForCategories(advancedImpactCalculationContainer, categoryImpactMap);
        } catch (Exception e) {
        }
    }

    private void calculateMonthlyImpact() {
        List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        if (huellas == null || huellas.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No hay huellas para calcular el impacto.");
            return;
        }
        try {
            Map<Integer, BigDecimal> weeklyImpactMap = OperationsUtils.calculateMonthlyImpact(huellas);
            showAlert("Impacto mensual", "El impacto medioambiental mensual ha sido calculado.");
            chartContainer.getChildren().clear();
            ChartUtils.showMonthlyImpactChart(chartContainer, weeklyImpactMap);
        } catch (Exception e) {
        }
    }

    private void calculateWeeklyImpact() {
        List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        if (huellas == null || huellas.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No hay huellas para calcular el impacto.");
            return;
        }
        try {
            BigDecimal totalImpact = OperationsUtils.calculateWeeklyImpact(huellas);
            showAlert("Impacto semanal", "El impacto medioambiental semanal es: " + totalImpact.toString() + "KG de CO2");
            chartContainer.getChildren().clear();
            ChartUtils.showWeeklyImpactChart(chartContainer, huellas);
        } catch (Exception e) {
        }
    }

    @FXML
    public void showRecommendationsDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("RecommendationsDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Recomendaciones Personalizadas");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {

        }
    }

    @FXML
    public void createFingerPrint() throws IOException {
        App.setRoot("CreateFingerPrint");
    }

    @FXML
    public void modifyHabit() throws IOException {
        App.setRoot("ModifyHabits");
    }

    @FXML
    public void createHabit() throws IOException {
        App.setRoot("CreateHabit");
    }

    @FXML
    public void modifyFingerprint() {
        Huella fingerprint = fingerprintTable.getSelectionModel().getSelectedItem();
        if (fingerprint != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("ModifyFingerprintDialog.fxml"));
                Parent parent = loader.load();
                ModifyFingerprintsDialogController controller = loader.getController();
                controller.setSelectedFingerPrint(fingerprint);
                controller.setMainMenuController(this);
                Stage stage = new Stage();
                stage.setTitle("Modificar huella");
                stage.setScene(new Scene(parent));
                stage.showAndWait();
                loadFingerprints();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            showAlert("Error", "Seleccione una huella para modificar.");
        }
    }

    private void showAlert(String title, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

    @FXML
    public void refreshTable() {
        loadFingerprints();
    }
}
package org.example.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.*;
import org.example.services.FingerprintService;
import org.example.services.HabitService;
import org.example.services.UserService;
import org.example.utils.AlertsUtils;
import org.example.utils.OperationsUtils;
import org.example.utils.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainMenuController {

    @FXML
    private TableView<Huella> fingerprintTable;

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
            BigDecimal impactoTotal = calculateImpactoTotal(usuario);
            return new SimpleObjectProperty<>(impactoTotal);
        });
        loadFingerprints();
        loadHabits();
        loadUsernames();
    }

    private BigDecimal calculateImpactoTotal(Usuario usuario) {
        List<Huella> huellas = fingerprintService.viewFingerPrints(usuario);
        BigDecimal totalImpact = huellas.stream()
                .map(huella -> {
                    BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                    return huella.getValor().multiply(factorEmision);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalImpact.setScale(2, RoundingMode.HALF_UP);
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
                default:
                    AlertsUtils.showErrorAlert("Error", "Selección no válida.");
            }
        }
    }
    private void loadFingerprints() {
        List<Huella> fingerprints = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
        if (fingerprints != null) {
            ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
            fingerprintTable.setItems(fingerprintList);
        }
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
            AlertsUtils.showErrorAlert("Error", "Hubo un error al cargar el diálogo de selección de huellas: " + e.getMessage());
        }
    }

    private void calculateImpactForAllFootprints() {
        try {
            List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
            BigDecimal totalImpact = OperationsUtils.calculateImpactForAllFootprints(huellas);
            AlertsUtils.showAlert("Impacto total medioambiental", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
            showBarChartForFingerprints(huellas);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto medioambiental: " + e.getMessage());
        }
    }

    public void calculateImpactForCategory(Categoria category) {
        try {
            List<Huella> huellas = fingerprintService.viewFingerPrintsByCategory(Session.getInstance().getUserLogged(), category);
            BigDecimal totalImpact = OperationsUtils.calculateImpactForCategory(huellas, category);
            AlertsUtils.showAlert("Impacto medioambiental por categoría", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto medioambiental por categoría: " + e.getMessage());
        }
    }

    private void calculateImpactForAllCategories() {
        try {
            List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
            Map<String, BigDecimal> categoryImpactMap = OperationsUtils.calculateImpactForAllCategories(huellas);
            showBarChartForCategories(categoryImpactMap);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto medioambiental por categoría: " + e.getMessage());
        }
    }

    private void calculateMonthlyImpact() {
        try {
            List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
            Map<Integer, BigDecimal> weeklyImpactMap = OperationsUtils.calculateMonthlyImpact(huellas);
            AlertsUtils.showAlert("Impacto mensual", "El impacto medioambiental mensual ha sido calculado.");
            showMonthlyImpactChart(weeklyImpactMap);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto mensual: " + e.getMessage());
        }
    }

    private void calculateWeeklyImpact() {
        try {
            List<Huella> huellas = fingerprintService.viewFingerPrints(Session.getInstance().getUserLogged());
            BigDecimal totalImpact = OperationsUtils.calculateWeeklyImpact(huellas);
            AlertsUtils.showAlert("Impacto semanal", "El impacto medioambiental semanal es: " + totalImpact.toString() + "KG de CO2");
            showWeeklyImpactChart(huellas);
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto semanal: " + e.getMessage());
        }
    }

    private void showMonthlyImpactChart(Map<Integer, BigDecimal> weeklyImpactMap) {
        chartContainer.getChildren().clear();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Semana");
        yAxis.setLabel("Impacto (KG de CO2)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto Mensual");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Semanas");

        for (Map.Entry<Integer, BigDecimal> entry : weeklyImpactMap.entrySet()) {
            series.getData().add(new XYChart.Data<>("Semana " + entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        chartContainer.getChildren().add(barChart);
    }

    private void showWeeklyImpactChart(List<Huella> huellas) {
        chartContainer.getChildren().clear();
        Map<Integer, BigDecimal> weeklyImpactMap = OperationsUtils.calculateWeeklyImpactMap(huellas);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Semana");
        yAxis.setLabel("Impacto (KG de CO2)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto Semanal");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Semanas");

        for (Map.Entry<Integer, BigDecimal> entry : weeklyImpactMap.entrySet()) {
            series.getData().add(new XYChart.Data<>("Semana " + entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        chartContainer.getChildren().add(barChart);
    }

    private void showBarChartForFingerprints(List<Huella> huellas) {
        advancedImpactCalculationContainer.getChildren().clear();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Actividad");
        yAxis.setLabel("Valor");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto de Huellas");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Huellas");

        for (Huella huella : huellas) {
            series.getData().add(new XYChart.Data<>(huella.getIdActividad().getNombre(), huella.getValor()));
        }

        barChart.getData().add(series);

        advancedImpactCalculationContainer.getChildren().add(barChart);
    }

    private void showBarChartForCategories(Map<String, BigDecimal> categoryImpactMap) {
        advancedImpactCalculationContainer.getChildren().clear();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Categoría");
        yAxis.setLabel("Impacto (KG de CO2)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto por Categoría");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Categorías");

        for (Map.Entry<String, BigDecimal> entry : categoryImpactMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        advancedImpactCalculationContainer.getChildren().add(barChart);
    }

    private void showAdvancedImpactChart(Map<String, BigDecimal> impactMap) {
        advancedImpactCalculationContainer.getChildren().clear();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Categoría");
        yAxis.setLabel("Impacto (KG de CO2)");

        final BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Impacto Avanzado");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Categorías");

        for (Map.Entry<String, BigDecimal> entry : impactMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        barChart.getData().add(series);

        advancedImpactCalculationContainer.getChildren().add(barChart);
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
            AlertsUtils.showErrorAlert("Error", "Hubo un error al cargar el diálogo de recomendaciones: " + e.getMessage());
        }
    }

    @FXML
    public void showAdvancedImpactCalculationDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("ImpactCalculationDialog.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Calcular Impacto Medioambiental");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al cargar el diálogo de cálculo de impacto avanzado: " + e.getMessage());
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
                Stage stage = new Stage();
                stage.setTitle("Modificiar huella");
                stage.setScene(new Scene(parent));
                stage.showAndWait();
                loadFingerprints();
            } catch (IOException e) {
                AlertsUtils.showErrorAlert("Error", "Failed to load modify fingerprint dialog: " + e.getMessage());
            }
        } else {
            AlertsUtils.showErrorAlert("Error", "No fingerprint selected.");
        }
    }
}
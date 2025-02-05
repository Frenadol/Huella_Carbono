package org.example.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.entities.Categoria;
import org.example.entities.Habito;
import org.example.entities.Huella;
import org.example.services.FingerprintService;
import org.example.services.HabitService;
import org.example.utils.AlertsUtils;
import org.example.utils.Session;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class MainMenuController {

    @FXML
    private TableView<Huella> fingerprintTable;

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
    private TableColumn<Habito, String> habitActivityNameColumn;

    @FXML
    private TableColumn<Habito, String> habitFrecuenciaColumn;

    @FXML
    private TableColumn<Habito, String> habitTipoColumn;

    @FXML
    private TableColumn<Habito, String> habitUltimaFechaColumn;

    private final FingerprintService fingerprintService = new FingerprintService();
    private final HabitService habitService = new HabitService();

    @FXML
    public void initialize() {
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        habitActivityNameColumn.setCellValueFactory(cellData -> {
            Habito habito = cellData.getValue();
            String activityName = habito.getIdActividad() != null ? habito.getIdActividad().getNombre() : null;
            return new SimpleStringProperty(activityName);
        });
        habitFrecuenciaColumn.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        habitTipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        habitUltimaFechaColumn.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));
        loadFingerprints();
        loadHabits();
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
                showCategorySelectionDialog();
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
            BigDecimal totalImpact = BigDecimal.ZERO;

            for (Huella huella : huellas) {
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                totalImpact = totalImpact.add(impacto);
            }

            totalImpact = totalImpact.setScale(2, RoundingMode.HALF_UP);
            AlertsUtils.showAlert("Impacto total medioambiental", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto medioambiental: " + e.getMessage());
        }
    }

    private void showCategorySelectionDialog() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("CategorySelectionDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setTitle("Seleccionar Categoría");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al cargar el diálogo de selección de categoría: " + e.getMessage());
        }
    }

    public void calculateImpactForCategory(Categoria category) {
        try {
            List<Huella> huellas = fingerprintService.viewFingerPrintsByCategory(Session.getInstance().getUserLogged(), category);
            BigDecimal totalImpact = BigDecimal.ZERO;

            for (Huella huella : huellas) {
                BigDecimal factorEmision = BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision());
                BigDecimal impacto = huella.getValor().multiply(factorEmision);
                totalImpact = totalImpact.add(impacto);
            }

            totalImpact = totalImpact.setScale(2, RoundingMode.HALF_UP);
            AlertsUtils.showAlert("Impacto medioambiental por categoría", "El impacto medioambiental producido es: " + totalImpact.toString() + "KG de CO2");
        } catch (Exception e) {
            AlertsUtils.showErrorAlert("Error", "Hubo un error al calcular el impacto medioambiental por categoría: " + e.getMessage());
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
            AlertsUtils.showErrorAlert("Error", "Hubo un error al cargar el diálogo de recomendaciones: " + e.getMessage());
        }
    }
    @FXML
    public void showAdvancedImpactCalculationDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ImpactCalculationDialog.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Calcular Impacto Medioambiental");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
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
                controller.setFingerprint(fingerprint);
                Stage stage = new Stage();
                stage.setTitle("Modify Fingerprint");
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
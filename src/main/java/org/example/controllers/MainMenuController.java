package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.App;
import org.example.dao.FingerPrintDao;
import org.example.entities.Huella;
import org.example.utils.Session;

import java.io.IOException;
import java.util.List;

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
    public void initialize() {
        activityColumn.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valor"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidad"));

        loadFingerprints();
    }

    private void loadFingerprints() {
        FingerPrintDao fingerprintDao = new FingerPrintDao();
        List<Huella> fingerprints = fingerprintDao.viewFingerPrints(Session.getInstance().getUserLogged());
        ObservableList<Huella> fingerprintList = FXCollections.observableArrayList(fingerprints);
        fingerprintTable.setItems(fingerprintList);
    }

    @FXML
    public void createFingerPrint() throws IOException {
        App.setRoot("CreateFingerPrint");
    }
    @FXML
    public void createHabit() throws IOException {
        App.setRoot("CreateHabit");
    }
}
package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import org.example.services.HabitService;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class HabitImpactController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label impactLabel;

    private HabitService habitService = new HabitService();

    @FXML
    public void calculateDailyImpact() {
        LocalDate date = datePicker.getValue();
        Usuario user = Session.getInstance().getUserLogged();
        List<Huella> huellas = habitService.getHuellasByUser(user);
        BigDecimal impact = calculateImpactForDay(huellas, date);
        impactLabel.setText("Daily Impact: " + impact + " KG of CO2");
    }

    @FXML
    public void calculateWeeklyImpact() {
        LocalDate date = datePicker.getValue();
        Usuario user = Session.getInstance().getUserLogged();
        List<Huella> huellas = habitService.getHuellasByUser(user);
        BigDecimal impact = calculateImpactForWeek(huellas, date);
        impactLabel.setText("Weekly Impact: " + impact + " KG of CO2");
    }

    @FXML
    public void calculateMonthlyImpact() {
        LocalDate date = datePicker.getValue();
        Usuario user = Session.getInstance().getUserLogged();
        List<Huella> huellas = habitService.getHuellasByUser(user);
        BigDecimal impact = calculateImpactForMonth(huellas, date);
        impactLabel.setText("Monthly Impact: " + impact + " KG of CO2");
    }

    private BigDecimal calculateImpactForDay(List<Huella> huellas, LocalDate date) {
        return huellas.stream()
                .filter(huella -> huella.getFecha().toLocalDate().isEqual(date))
                .map(huella -> huella.getValor().multiply(BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateImpactForWeek(List<Huella> huellas, LocalDate startDate) {
        LocalDate endDate = startDate.plus(1, ChronoUnit.WEEKS);
        return huellas.stream()
                .filter(huella -> !huella.getFecha().toLocalDate().isBefore(startDate) && huella.getFecha().toLocalDate().isBefore(endDate))
                .map(huella -> huella.getValor().multiply(BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateImpactForMonth(List<Huella> huellas, LocalDate date) {
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = startDate.plus(1, ChronoUnit.MONTHS);
        return huellas.stream()
                .filter(huella -> !huella.getFecha().toLocalDate().isBefore(startDate) && huella.getFecha().toLocalDate().isBefore(endDate))
                .map(huella -> huella.getValor().multiply(BigDecimal.valueOf(huella.getIdActividad().getIdCategoria().getFactorEmision())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
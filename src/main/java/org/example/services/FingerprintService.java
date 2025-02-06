package org.example.services;

import org.example.dao.FingerPrintDao;
import org.example.entities.Actividad;
import org.example.entities.Categoria;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.AlertsUtils;
import org.example.utils.Connection;
import org.example.utils.OperationsUtils;
import org.example.utils.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class FingerprintService {

    public boolean saveFingerprint(Huella huella) {
        if (huella == null || huella.getIdUsuario() == null || huella.getIdActividad() == null || huella.getValor() == null || huella.getUnidad() == null) {
            AlertsUtils.showErrorAlert("Error", "Datos de la huella inválidos.");
            return false;
        }
        FingerPrintDao.build().saveFingerPrint(huella);
        AlertsUtils.showAlert("Éxito", "Huella registrada correctamente.");
        return true;
    }
    public List<Huella> viewFingerPrints(Usuario user) {
        List<Huella> fingerprints = FingerPrintDao.build().viewFingerPrints(user);
        if (fingerprints == null || fingerprints.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No fingerprints found.");
        }
        return fingerprints;
    }

    public List<Huella> viewFingerPrintsByCategory(Usuario user, Categoria category) {
        List<Huella> fingerprints = FingerPrintDao.build().viewFingerPrintsByCategory(user, category);
        if (fingerprints == null || fingerprints.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No fingerprints found for the selected category.");
        }
        return fingerprints;
    }

    public List<Huella> viewFingerPrints() {
        List<Huella> fingerprints = FingerPrintDao.build().viewFingerPrints(Session.getInstance().getUserLogged());
        if (fingerprints == null || fingerprints.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No fingerprints found.");
        }
        return fingerprints;
    }
    public void deleteFingerPrint(Huella huella) {
        FingerPrintDao.build().deleteFingerPrint(huella);
    }
    public Map<String, BigDecimal> getUserCarbonFootprintByCategory(Usuario usuario) {
        List<Huella> huellas = FingerPrintDao.build().viewFingerPrints(usuario);
        return OperationsUtils.calculateImpactByCategory(huellas);
    }

    public Map<String, BigDecimal> getAverageCarbonFootprintByCategory() {
        List<Huella> allHuellas = FingerPrintDao.build().getAllFingerprints();
        return OperationsUtils.calculateAverageImpactByCategory(allHuellas);
    }
    public BigDecimal calculateTotalImpact() {
        List<Huella> allFingerprints = FingerPrintDao.build().getAllFingerprints();
        BigDecimal totalImpact = BigDecimal.ZERO;
        for (Huella huella : allFingerprints) {
            totalImpact = totalImpact.add(huella.getValor());
        }
        return totalImpact;
    }

    public void updateFingerPrintDetails(Huella huella, Actividad nuevaActividad, BigDecimal nuevoValor, String nuevaUnidad, LocalDateTime nuevaFecha) {
        try (org.hibernate.Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            huella.setIdActividad(nuevaActividad);
            huella.setValor(nuevoValor);
            huella.setUnidad(nuevaUnidad);
            huella.setFecha(nuevaFecha);
            session.update(huella);
            session.getTransaction().commit();
        }
    }
}
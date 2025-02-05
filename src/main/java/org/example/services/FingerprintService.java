package org.example.services;

import org.example.dao.FingerPrintDao;
import org.example.entities.Categoria;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.AlertsUtils;
import org.example.utils.Session;

import java.util.List;

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
}
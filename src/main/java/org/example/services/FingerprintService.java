package org.example.services;

import org.example.dao.FingerPrintDao;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.entities.Session;
import org.example.entities.Usuario;
import org.example.utils.AlertsUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing fingerprints.
 * This class provides methods to interact with the FingerPrintDao for `Huella` entities.
 */
public class FingerprintService {

    /**
     * Saves a new `Huella` entity.
     *
     * @param huella the `Huella` entity to save.
     * @return true if the fingerprint was saved successfully, false otherwise.
     */
    public boolean saveFingerprint(Huella huella) {
        if (huella == null || huella.getIdUsuario() == null || huella.getIdActividad() == null || huella.getValor() == null || huella.getUnidad() == null) {
            AlertsUtils.showErrorAlert("Error", "Datos de la huella inválidos.");
            return false;
        }
        FingerPrintDao.build().saveFingerPrint(huella);
        AlertsUtils.showAlert("Éxito", "Huella registrada correctamente.");
        return true;
    }

    /**
     * Retrieves all `Huella` entities for a given user.
     *
     * @param user the `Usuario` whose fingerprints to retrieve.
     * @return a list of `Huella` entities.
     */
    public List<Huella> viewFingerPrints(Usuario user) {
        List<Huella> fingerprints = FingerPrintDao.build().viewFingerPrints(user);
        if (fingerprints == null || fingerprints.isEmpty()) {
        }
        return fingerprints;
    }

    /**
     * Retrieves all `Huella` entities for the logged-in user.
     *
     * @return a list of `Huella` entities.
     */
    public List<Huella> viewFingerPrints() {
        List<Huella> fingerprints = FingerPrintDao.build().viewFingerPrints(Session.getInstance().getUserLogged());
        if (fingerprints == null || fingerprints.isEmpty()) {
        }
        return fingerprints;
    }

    /**
     * Deletes a `Huella` entity.
     *
     * @param huella the `Huella` entity to delete.
     */
    public void deleteFingerPrint(Huella huella) {
        if (huella == null || huella.getId() == null) {
            AlertsUtils.showErrorAlert("Error", "Datos de la huella inválidos.");
            return;
        }
        FingerPrintDao.build().deleteFingerPrint(huella);
    }

    /**
     * Updates the details of a `Huella` entity.
     *
     * @param huella the `Huella` entity to update.
     * @param nuevaActividad the new `Actividad` entity.
     * @param nuevoValor the new value.
     * @param nuevaUnidad the new unit.
     * @param nuevaFecha the new date.
     */
    public void updateFingerPrintDetails(Huella huella, Actividad nuevaActividad, BigDecimal nuevoValor, String nuevaUnidad, LocalDateTime nuevaFecha) {
        if (huella == null || huella.getId() == null || nuevaActividad == null || nuevoValor == null || nuevaUnidad == null || nuevaFecha == null) {
            AlertsUtils.showErrorAlert("Error", "Datos de la huella o de la actualización inválidos.");
            return;
        }
        FingerPrintDao.build().updateFingerPrintDetails(huella, nuevaActividad, nuevoValor, nuevaUnidad, nuevaFecha);
    }
}
package org.example.dao;

import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.connection.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Access Object (DAO) class for managing `Huella` entities.
 * This class provides methods to interact with the database for `Huella` entities.
 */
public class FingerPrintDao {

    /**
     * Builds and returns an instance of `FingerPrintDao`.
     *
     * @return a new instance of `FingerPrintDao`.
     */
    public static FingerPrintDao build() {
        return new FingerPrintDao();
    }

    /**
     * Saves a `Huella` entity to the database.
     *
     * @param huella the `Huella` entity to save.
     */
    public void saveFingerPrint(Huella huella) {
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            session.save(huella);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Deletes a `Huella` entity from the database.
     *
     * @param huella the `Huella` entity to delete.
     */
    public void deleteFingerPrint(Huella huella) {
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            session.delete(huella);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Updates the details of a `Huella` entity in the database.
     *
     * @param huella the `Huella` entity to update.
     * @param nuevaActividad the new `Actividad` to set.
     * @param nuevoValor the new value to set.
     * @param nuevaUnidad the new unit to set.
     * @param nuevaFecha the new date to set.
     */
    public void updateFingerPrintDetails(Huella huella, Actividad nuevaActividad, BigDecimal nuevoValor, String nuevaUnidad, LocalDateTime nuevaFecha) {
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            huella.setIdActividad(nuevaActividad);
            huella.setValor(nuevoValor);
            huella.setUnidad(nuevaUnidad);
            huella.setFecha(nuevaFecha);
            session.update(huella);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Retrieves all `Huella` entities for a given user from the database.
     *
     * @param usuario the `Usuario` whose fingerprints to retrieve.
     * @return a list of `Huella` entities.
     */
    public List<Huella> viewFingerPrints(Usuario usuario) {
        Session session = null;
        List<Huella> huellas = null;
        try {
            session = Connection.getInstance().getSession();
            Query<Huella> query = session.createQuery(
                    "FROM Huella h JOIN FETCH h.idActividad a JOIN FETCH a.idCategoria WHERE h.idUsuario = :usuario",
                    Huella.class
            );
            query.setParameter("usuario", usuario);
            huellas = query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return huellas;
    }
}
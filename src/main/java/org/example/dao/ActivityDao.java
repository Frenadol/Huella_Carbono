package org.example.dao;

import org.example.entities.Actividad;
import org.example.connection.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data Access Object (DAO) class for managing `Actividad` entities.
 * This class provides methods to interact with the database for `Actividad` entities.
 */
public class ActivityDao {

    /**
     * Retrieves all `Actividad` entities along with their associated categories.
     *
     * @return a list of `Actividad` entities with their categories.
     */
    public List<Actividad> findAllWithCategories() {
        Session session = null;
        List<Actividad> actividades = null;
        try {
            session = Connection.getInstance().getSession();
            Query<Actividad> query = session.createQuery("SELECT a FROM Actividad a JOIN a.idCategoria", Actividad.class);
            actividades = query.getResultList();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return actividades;
    }

    /**
     * Builds and returns an instance of `ActivityDao`.
     *
     * @return a new instance of `ActivityDao`.
     */
    public static ActivityDao build() {
        return new ActivityDao();
    }
}
package org.example.dao;

import org.example.entities.Recomendacion;
import org.example.connection.Connection;
import org.hibernate.Session;

import java.util.List;

/**
 * Data Access Object (DAO) class for managing `Recomendacion` entities.
 * This class provides methods to interact with the database for `Recomendacion` entities.
 */
public class RecommendationDao {

    private Connection connection = Connection.getInstance();

    /**
     * Retrieves a list of `Recomendacion` entities for a given user ID.
     *
     * @param userId the ID of the user whose recommendations to retrieve.
     * @return a list of `Recomendacion` entities.
     */
    public List<Recomendacion> getRecommendationsByUserId(int userId) {
        Session session = null;
        List<Recomendacion> recommendations = null;
        try {
            session = connection.getSession();
            recommendations = session.createQuery(
                            "SELECT r FROM Recomendacion r " +
                                    "JOIN r.idCategoria c " +
                                    "JOIN c.actividades a " +
                                    "JOIN Habito h ON a.id = h.idActividad.id " +
                                    "WHERE h.idUsuario.id = :userId", Recomendacion.class)
                    .setParameter("userId", userId)
                    .list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return recommendations;
    }
}
// src/main/java/org/example/dao/RecommendationDao.java
package org.example.dao;

import org.example.entities.Recomendacion;
import org.example.utils.Connection;
import org.hibernate.Session;

import java.util.List;

public class RecommendationDao {

    private Connection connection = Connection.getInstance();

    public List<Recomendacion> getRecommendationsByUserId(int userId) {
        List<Recomendacion> recommendations = null;
        try (Session session = connection.getSession()) {
            recommendations = session.createQuery(
                            "SELECT r FROM Recomendacion r " +
                                    "JOIN r.idCategoria c " +
                                    "JOIN c.actividades a " +
                                    "JOIN Habito h ON a.id = h.idActividad.id " +
                                    "WHERE h.idUsuario.id = :userId", Recomendacion.class)
                    .setParameter("userId", userId)
                    .list();
        }
        return recommendations;
    }
}
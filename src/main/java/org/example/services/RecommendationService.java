// src/main/java/org/example/services/RecommendationService.java
package org.example.services;

import org.example.dao.RecommendationDao;
import org.example.entities.Recomendacion;
import org.example.utils.AlertsUtils;

import java.util.Collections;
import java.util.List;

/**
 * Service class for managing recommendations.
 * This class provides methods to interact with the RecommendationDao for `Recomendacion` entities.
 */
public class RecommendationService {

    private RecommendationDao recommendationDao = new RecommendationDao();

    /**
     * Retrieves personalized recommendations for a given user.
     *
     * @param userId the ID of the user to retrieve recommendations for.
     * @return a list of `Recomendacion` entities. If the user ID is invalid, returns an empty list.
     */
    public List<Recomendacion> getPersonalizedRecommendations(int userId) {
        if (userId <= 0) {
            AlertsUtils.showErrorAlert("Error", "No se encontro ID del usuario.");
            return Collections.emptyList();
        }
        return recommendationDao.getRecommendationsByUserId(userId);
    }
}
// src/main/java/org/example/services/RecommendationService.java
package org.example.services;

import org.example.dao.RecommendationDao;
import org.example.entities.Recomendacion;

import java.util.List;

public class RecommendationService {

    private RecommendationDao recommendationDao = new RecommendationDao();

    public List<Recomendacion> getPersonalizedRecommendations(int userId) {
        return recommendationDao.getRecommendationsByUserId(userId);
    }
}
package org.example.services;

import org.example.dao.ActivityDao;
import org.example.entities.Actividad;
import org.example.utils.AlertsUtils;

import java.util.List;

/**
 * Service class for managing activities.
 * This class provides methods to interact with the ActivityDao for `Actividad` entities.
 */
public class ActivityService {

    /**
     * Retrieves all `Actividad` entities with their associated categories.
     *
     * @return a list of `Actividad` entities.
     */
    public List<Actividad> findAllWithCategories() {
        List<Actividad> activities = ActivityDao.build().findAllWithCategories();
        if (activities == null || activities.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No actividades encontradas.");
        }
        return activities;
    }
}
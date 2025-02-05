package org.example.services;

import org.example.dao.ActivityDao;
import org.example.entities.Actividad;
import org.example.utils.AlertsUtils;

import java.util.List;

public class ActivityService {

    public List<Actividad> findAllWithCategories() {
        List<Actividad> activities = ActivityDao.build().findAllWithCategories();
        if (activities == null || activities.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No activities found.");
        }
        return activities;
    }
}
// src/main/java/org/example/services/HabitService.java
package org.example.services;

import org.example.dao.HabitDao;
import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.example.utils.AlertsUtils;

public class HabitService {

    private HabitDao habitDao;

    public HabitService() {
        this.habitDao = new HabitDao();
    }

    public boolean insertHabit(Habito newHabit) {
        if (habitDao.exists(newHabit.getId())) {
            AlertsUtils.showErrorAlert("Error", "El hábito ya existe. Solo puedes cambiar la fecha del hábito.");
            return false;
        } else {
            habitDao.createHabit(newHabit);
            return true;
        }
    }
}
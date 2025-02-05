package org.example.services;

import org.example.dao.FingerPrintDao;
import org.example.dao.HabitDao;
import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.example.entities.Huella;
import org.example.entities.Usuario;
import org.example.utils.AlertsUtils;

import java.util.List;

public class HabitService {

    private final HabitDao habitDao;

    public HabitService() {
        this.habitDao = HabitDao.build();
    }
    public List<Huella> getHuellasByUser(Usuario usuario) {
        return FingerPrintDao.build().viewFingerPrints(usuario);
    }

    public boolean insertHabit(Habito newHabit) {
        if (newHabit == null || newHabit.getId() == null || newHabit.getIdUsuario() == null) {
            AlertsUtils.showErrorAlert("Error", "Datos del hábito inválidos.");
            return false;
        }
        if (habitDao.exists(newHabit.getId())) {
            AlertsUtils.showErrorAlert("Error", "El hábito ya existe. Solo puedes cambiar la fecha del hábito.");
            return false;
        } else {
            habitDao.createHabit(newHabit);
            return true;
        }
    }

    public List<Habito> getHabitsByUser(Usuario usuario) {
        if (usuario == null) {
            AlertsUtils.showErrorAlert("Error", "Usuario inválido.");
            return null;
        }
        return habitDao.getHabitsByUser(usuario);
    }

    public boolean deleteHabit(Habito habito) {
        if (habito == null || habito.getId() == null) {
            AlertsUtils.showErrorAlert("Error", "Datos del hábito inválidos.");
            return false;
        }
        habitDao.delete(habito);
        AlertsUtils.showAlert("Success", "Habit deleted successfully.");
        return true;
    }
    public void updateHabit(Habito habit) {
        if (habit == null || habit.getId() == null) {
            AlertsUtils.showErrorAlert("Error", "Datos del hábito inválidos.");
            return;
        }
        habitDao.updateHabit(habit);}
}
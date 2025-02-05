package org.example.dao;

import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.example.entities.Usuario;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HabitDao {

    public void createHabit(Habito habito) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.save(habito);
            session.getTransaction().commit();
        }
    }

    public boolean exists(HabitoId habitoId) {
        try (Session session = Connection.getInstance().getSession()) {
            Habito habito = session.get(Habito.class, habitoId);
            return habito != null;
        }
    }

    public List<Habito> getHabitsByUser(Usuario usuario) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            Query<Habito> getHabitsByUserQuery = session.createQuery("From Habito where idUsuario=:usuario", Habito.class);
            getHabitsByUserQuery.setParameter("usuario", usuario);
            List<Habito> userHabits = getHabitsByUserQuery.list();
            session.getTransaction().commit();
            return userHabits;
        }
    }

    public static HabitDao build() {
        return new HabitDao();
    }

    public void updateHabit(Habito habit) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.update(habit);
            session.getTransaction().commit();
        }
    }

    public void delete(Habito habito) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.delete(habito);
            session.getTransaction().commit();
        }
    }
}
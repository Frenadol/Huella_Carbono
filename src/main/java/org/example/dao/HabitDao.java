package org.example.dao;

import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.example.entities.Usuario;
import org.example.connection.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data Access Object (DAO) class for managing `Habito` entities.
 * This class provides methods to interact with the database for `Habito` entities.
 */
public class HabitDao {

    /**
     * Creates a new `Habito` entity in the database.
     *
     * @param habito the `Habito` entity to create.
     */
    public void createHabit(Habito habito) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.save(habito);
            session.getTransaction().commit();
        }
    }

    /**
     * Checks if a `Habito` entity exists in the database.
     *
     * @param habitoId the ID of the `Habito` entity to check.
     * @return true if the `Habito` entity exists, false otherwise.
     */
    public boolean exists(HabitoId habitoId) {
        try (Session session = Connection.getInstance().getSession()) {
            Habito habito = session.get(Habito.class, habitoId);
            return habito != null;
        }
    }

    /**
     * Retrieves all `Habito` entities for a given user from the database.
     *
     * @param usuario the `Usuario` whose habits to retrieve.
     * @return a list of `Habito` entities.
     */
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

    /**
     * Builds and returns an instance of `HabitDao`.
     *
     * @return a new instance of `HabitDao`.
     */
    public static HabitDao build() {
        return new HabitDao();
    }

    /**
     * Updates an existing `Habito` entity in the database.
     *
     * @param habit the `Habito` entity to update.
     */
    public void updateHabit(Habito habit) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.update(habit);
            session.getTransaction().commit();
        }
    }

    /**
     * Deletes a `Habito` entity from the database.
     *
     * @param habito the `Habito` entity to delete.
     */
    public void delete(Habito habito) {
        try (Session session = Connection.getInstance().getSession()) {
            session.beginTransaction();
            session.delete(habito);
            session.getTransaction().commit();
        }
    }
}
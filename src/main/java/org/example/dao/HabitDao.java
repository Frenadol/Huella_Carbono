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
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            session.save(habito);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Checks if a `Habito` entity exists in the database.
     *
     * @param habitoId the ID of the `Habito` entity to check.
     * @return true if the `Habito` entity exists, false otherwise.
     */
    public boolean exists(HabitoId habitoId) {
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            Habito habito = session.get(Habito.class, habitoId);
            return habito != null;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Retrieves all `Habito` entities for a given user from the database.
     *
     * @param usuario the `Usuario` whose habits to retrieve.
     * @return a list of `Habito` entities.
     */
    public List<Habito> getHabitsByUser(Usuario usuario) {
        Session session = null;
        List<Habito> userHabits = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            Query<Habito> getHabitsByUserQuery = session.createQuery(
                    "FROM Habito h JOIN FETCH h.idActividad a JOIN FETCH a.idCategoria WHERE h.idUsuario = :usuario",
                    Habito.class
            );
            getHabitsByUserQuery.setParameter("usuario", usuario);
            userHabits = getHabitsByUserQuery.list();
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return userHabits;
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
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            session.update(habit);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * Deletes a `Habito` entity from the database.
     *
     * @param habito the `Habito` entity to delete.
     */
    public void delete(Habito habito) {
        Session session = null;
        try {
            session = Connection.getInstance().getSession();
            session.beginTransaction();
            session.delete(habito);
            session.getTransaction().commit();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
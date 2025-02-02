package org.example.dao;

import org.example.entities.Actividad;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ActivityDao {
    public List<Actividad> findAll() {
        Query<Actividad> findAllActivities = Connection.getInstance().getSession().createQuery("FROM Actividad", Actividad.class);
        List<Actividad> activities = findAllActivities.list();
        return activities;
    }

    public List<Actividad> findAllWithCategories() {
        Session session = Connection.getInstance().getSession();
        Query<Actividad> query = session.createQuery("SELECT a FROM Actividad a JOIN a.idCategoria", Actividad.class);
        return query.getResultList();

    }

    public List<Actividad> findActivitiesByUserId(int userid) {
        List<Actividad> activities = null;
        try (Session session = Connection.getInstance().getSession()) {
            Query<Actividad> query = session.createQuery("SELECT a FROM Actividad a JOIN a.usuarios u WHERE u.id = :id", Actividad.class);
            query.setParameter("id", userid);
            activities = query.list();
        }
        return activities;
    }
}
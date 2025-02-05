package org.example.dao;

import org.example.entities.Actividad;
import org.example.utils.Connection;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ActivityDao {
    public List<Actividad> findAllWithCategories() {
        Session session = Connection.getInstance().getSession();
        Query<Actividad> query = session.createQuery("SELECT a FROM Actividad a JOIN a.idCategoria", Actividad.class);
        return query.getResultList();

    }
    public static ActivityDao build() {
        return new ActivityDao();
    }

}
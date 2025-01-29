package org.example.dao;

import org.example.entities.Actividad;
import org.example.utils.Connection;
import org.hibernate.query.Query;

import java.util.List;

public class ActivityDao {
    public List<Actividad> findAll(){
        Query<Actividad> findAllActivities= Connection.getInstance().getSession().createQuery("FROM Actividad",Actividad.class);
        List<Actividad> activities=findAllActivities.list();
        return activities;
    }
}

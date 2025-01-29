package org.example.dao;

import org.example.entities.Actividad;
import org.example.entities.Categoria;
import org.example.utils.Connection;
import org.hibernate.query.Query;

import java.util.List;

public class CategoryDao {
    public List<Categoria> findAllCategories(){
        Query<Categoria> findAllActivities= Connection.getInstance().getSession().createQuery("FROM Categoria ",Categoria.class);
        List<Categoria> categories=findAllActivities.list();
        return categories;
    }
}

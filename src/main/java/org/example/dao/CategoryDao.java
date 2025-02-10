package org.example.dao;

import org.example.entities.Categoria;
import org.example.connection.Connection;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Data Access Object (DAO) class for managing `Categoria` entities.
 * This class provides methods to interact with the database for `Categoria` entities.
 */
public class CategoryDao {

    /**
     * Retrieves all `Categoria` entities from the database.
     *
     * @return a list of `Categoria` entities.
     */
    public List<Categoria> findAllCategories() {
        Query<Categoria> findAllActivities = Connection.getInstance().getSession().createQuery("FROM Categoria", Categoria.class);
        List<Categoria> categories = findAllActivities.list();
        return categories;
    }

    /**
     * Builds and returns an instance of `CategoryDao`.
     *
     * @return a new instance of `CategoryDao`.
     */
    public static CategoryDao build() {
        return new CategoryDao();
    }
}
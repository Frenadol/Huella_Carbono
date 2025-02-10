package org.example.services;

import org.example.dao.CategoryDao;
import org.example.entities.Categoria;
import org.example.utils.AlertsUtils;

import java.util.List;

/**
 * Service class for managing categories.
 * This class provides methods to interact with the CategoryDao for `Categoria` entities.
 */
public class CategoryService {

    /**
     * Retrieves all `Categoria` entities from the database.
     *
     * @return a list of `Categoria` entities.
     */
    public List<Categoria> findAllCategories() {
        List<Categoria> categories = CategoryDao.build().findAllCategories();
        if (categories == null || categories.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No categories found.");
        }
        return categories;
    }
}
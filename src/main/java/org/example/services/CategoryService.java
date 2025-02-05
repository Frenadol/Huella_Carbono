package org.example.services;

import org.example.dao.CategoryDao;
import org.example.entities.Categoria;
import org.example.utils.AlertsUtils;

import java.util.List;

public class CategoryService {

    public List<Categoria> findAllCategories() {
        List<Categoria> categories = CategoryDao.build().findAllCategories();
        if (categories == null || categories.isEmpty()) {
            AlertsUtils.showErrorAlert("Error", "No categories found.");
        }
        return categories;
    }
}
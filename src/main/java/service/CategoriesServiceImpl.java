package service;

import dao.CategoriesDAO;
import entities.Categories;

import java.util.List;

public class CategoriesServiceImpl implements CategoriesService {

    private CategoriesDAO categoriesDAO;

    public void setCategoriesDAO(CategoriesDAO categoriesDAO) {
        this.categoriesDAO = categoriesDAO;
    }

    public void addCategory(Categories categories) {
        categoriesDAO.addCategory(categories);
    }

    public void updateCategory(Categories categories) {
        categoriesDAO.updateCategory(categories);
    }

    public List<Categories> listCategories() {
        return categoriesDAO.listCategories();
    }

    public Categories getCategoryById(int id) {
        return categoriesDAO.getCategoryById(id);
    }

    public void removeCategoryById(int id) {
        categoriesDAO.removeCategoryById(id);
    }
}
